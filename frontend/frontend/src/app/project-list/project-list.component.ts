import { CommonModule  } from '@angular/common';
import { Project } from '../project';
import { ProjectService } from './../project.service';
import { ChangeDetectionStrategy, Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ProjectDialog } from '../project-dialog/project-dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatMenu, MatMenuTrigger } from '@angular/material/menu';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {MatButtonModule} from '@angular/material/button';
import {MatTableModule} from '@angular/material/table';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [MatIconModule, MatMenu, MatMenuTrigger, CommonModule, MatProgressSpinnerModule, MatButtonModule, MatTableModule ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProjectListComponent implements OnInit {
openProjectTasks(_t7: Project) {
throw new Error('Method not implemented.');
}

  private projectService : ProjectService = inject(ProjectService);
  private dialog: MatDialog = inject(MatDialog);
  projects: WritableSignal<Project[]> = signal([]);
  errorMessage = signal('');
  isLoading = signal(true);
  displayedColumns: string[] = ['name', 'active'];

  ngOnInit() {
    this.getProjects();
  }

  getProjects() {
    this.projectService.getAllProjects().subscribe(
      {
        next: data => {
          this.projects.set(data)
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
      width: '500px',
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
      width: '500px',
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

}
