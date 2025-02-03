import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from './../project.service';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../login/auth.service';
import { Project } from '../project';
import { ProjectEditDialogComponent } from '../edit-project/edit-project.component';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
})
export class ProjectListComponent implements OnInit {
  public projects: Project[];
  public requestCompleteOrFailed: boolean;
  public errorMessage: string = '';
  public errorType: 'auth' | 'loading' | null = null;

  constructor(
    private projectService: ProjectService,
    private taskService: TaskService,
    private router: Router,
    private dialog: MatDialog,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(() => {
      this.getProjectList();
    });

    this.projectService.projectCreated$.subscribe(() => {
      console.log('Project list updated due to creation');
      this.getProjectList();
    });
  }

  getProjectList() {
    if (!this.authService.isUserLoggedIn()) {
      this.errorType = 'auth';
      this.errorMessage = 'Please log in to view your projects';
      this.requestCompleteOrFailed = true;
      return;
    }

    this.projectService.getAllProjects().subscribe(
      (projects) => {
        this.projects = projects;
        this.errorType = null;
        this.errorMessage = '';
      },
      (error) => {
        console.error('Error:', error);
        this.requestCompleteOrFailed = true;
      },
      () => (this.requestCompleteOrFailed = true)
    );
  }

  createProject() {
    // Implement project creation logic
    const dialogRef = this.dialog.open(ProjectEditDialogComponent, {
      width: '500px',
      data: { ...new Project(), title: 'Create project' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.projectService.createProject(result).subscribe({
          next: () => {
            console.log('Project successfully created!');
          },
          error: (error) => {
            console.error('Error creating project:', error);
          },
        })
      }
    });
  }

  editProject(project: Project) {
    /*     const dialogRef = this.dialog.open(ProjectEditDialogComponent, {
      width: '500px',
      data: { project }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.getProjectList();
      }
    }); */
  }

  toggleProjectStatus(project: Project) {
    project.active = !project.active;
    this.projectService.updateProject(project.projectId, project).subscribe({
      next: () => {
        console.log('Project successfully updated!');
      },
      error: (error) => {
        console.error('Error updating project:', error);
      },
    })
  }

  openProjectTasks(project: Project) {
    this.router.navigate(['tasks/filter'], {
      queryParams: {
        projectId: project.projectId,
        status: null,
        priority: null,
      },
      queryParamsHandling: 'merge',
    });
  }

  deleteProject(project: Project) {
    /*     const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Delete Project',
        message: `Are you sure you want to delete "${project.projectName}"?`,
        confirmText: 'Delete',
        cancelText: 'Cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Implement delete logic
      }
    }); */
  }
}
