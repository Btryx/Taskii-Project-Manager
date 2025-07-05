import { CommonModule  } from '@angular/common';
import { Project } from '../project';
import { ProjectService } from './../project.service';
import { ChangeDetectionStrategy, Component, effect, inject, OnInit, signal, ViewChild, WritableSignal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ProjectDialog } from '../project-dialog/project-dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { Auth } from '../auth.service';
import { Observable } from 'rxjs';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { InfoPopup } from '../info-popup/info-popup';
import { Router } from '@angular/router';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatTooltipModule} from '@angular/material/tooltip';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [MatIconModule, MatMenu, MatMenuTrigger, CommonModule, MatProgressSpinnerModule, MatButtonModule,
     MatPaginatorModule, MatMenuItem, MatPaginator, MatTableModule, MatInputModule, MatFormFieldModule, MatSort, MatSortModule, MatTooltipModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProjectListComponent implements OnInit {

  private projectService : ProjectService = inject(ProjectService);
  private authService = inject(Auth);
  private dialog: MatDialog = inject(MatDialog);
  private router : Router = inject(Router);

  projects: WritableSignal<Project[]> = signal([]);
  usernames = signal(new Map<string, string>);

  errorMessage = signal('');
  isLoading = signal(true);
  displayedColumns: string[] = ['projectName', 'owner', 'createdAt', 'active', 'menu'];

  dataSource = new MatTableDataSource<Project>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      this.dataSource.data = this.projects();
    });
  }

  ngOnInit() {
    this.getProjects();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if(this.dataSource.filter == 'active') {
      this.dataSource.filter = 'true';
    }

    if(this.dataSource.filter == 'inactive') {
      this.dataSource.filter = 'false';
    }

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getProjects() {
    this.projectService.getAllProjects().subscribe(
      {
        next: data => {
          this.projects.set(data)
          const userIds = this.projects().map(p => p.userId);

          userIds.forEach(id => {
            this.getUserName(id).subscribe({
              next: data => {
                let username = data.message;

                this.usernames.update(map => {
                  let newMap = new Map(map);
                  newMap.set(id, username);
                  return newMap;
                });
              },
              error: error => {
                //do nothing, table column will remain empty
                console.error(error.error.message);
              }
            })
          });

        },
        error: (error: HttpErrorResponse) => {
          console.error(error);
          this.errorMessage.set(error.error.message);
        },
        complete: () => this.isLoading.set(false)
      }
    )
  }

  createProject() {
    const dialogRef = this.dialog.open(ProjectDialog, {
      data: { ...new Project(), title: 'Create project' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.projectService.createProject(result).subscribe({
          next: data => {
            this.projects.update(value => [...value, data]);
          },
          error: (error: HttpErrorResponse) => {
            console.error(error);
            this.errorMessage.set(error.error.message);
          },
        })
      }
    });
  }

  updateProject(project: Project) {
    const dialogRef = this.dialog.open(ProjectDialog, {
      data: { ...project, title: 'Edit project' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.projectService.updateProject(result, project.projectId).subscribe({
          next: () => {
            this.projects.update(value => {
              let indexOfUpdatedProject = value.findIndex(item => item.projectId === result.projectId);
              value.splice(indexOfUpdatedProject, 1, result);
              return [...value];
            });
          },
          error: (error: HttpErrorResponse) => {
            console.error(error);
            this.errorMessage.set(error.error.message);
          },
        })
      }
    });
  }

  deleteProject(project: Project) {
    if(project.active) {
      const dialogRef = this.dialog.open(InfoPopup, {
        disableClose: false
      });
          dialogRef.componentInstance.title = "Error"
      dialogRef.componentInstance.infoMessage = `Cannot delete active projects! Disable project before deleting.`
      return;
    }


    const dialogRef = this.dialog.open(ConfirmationDialog, {
      disableClose: false
    });
    dialogRef.componentInstance.title = "Delete project?"
    dialogRef.componentInstance.confirmMessage = `Are you sure you want to delete project: "${project.projectName}"?
                                                  This process cannot be undone!`
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.projectService.deleteProject(project.projectId).subscribe(
          {
            next: () => {
              this.projects.update(value => {
                let indexOfUpdatedProject = value.findIndex(item => item.projectId === project.projectId);
                value.splice(indexOfUpdatedProject, 1);
                return [...value];
              });
            },
            error: (error: HttpErrorResponse) => {
              console.error(error);
              this.errorMessage.set(error.error.message);
            },
          }
        )
      }
    });
  }

  toggleProjectStatus(project: Project) {
    project.active = !project.active;
    this.projectService.updateProject(project, project.projectId).subscribe({
      next: () => {
        this.projects.update(value => {
          let indexOfUpdatedProject = value.findIndex(item => item.projectId === project.projectId);
          value.splice(indexOfUpdatedProject, 1, project);
          return [...value];
        });
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
        this.errorMessage.set(error.error.message);
      },
    })
  }

  openProjectTasks(project: Project) {
    this.router.navigate(['tasks'], {
      queryParams: {
        projectId: project.projectId,
        status: null,
        priority: null,
      },
      queryParamsHandling: 'merge',
    });
  }

  getUserName(id: string): Observable<any> {
    return this.authService.getUser(id);
  }

  getColorForUser(id: string): string {
    return this.authService.getColorForUser(id);
  }

}
