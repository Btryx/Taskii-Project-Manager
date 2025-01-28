import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from './../project.service';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../login/auth.service';
import { Project } from '../project';
import { ProjectCreateDialogComponent } from '../create-project/create-project.component';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

  public projects: Project[]
  public requestCompleteOrFailed: boolean;
  public errorMessage: string = '';
  public errorType: 'auth' | 'loading' | null = null;

  constructor(public projectService : ProjectService,
              public router: Router,
              private dialog: MatDialog,
              public authService: AuthService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
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
      projects => {
        this.projects = projects;
        this.errorType = null;
        this.errorMessage = '';
      },
      error => {
        console.error('Error:', error);
        this.requestCompleteOrFailed = true;
      },
      () => this.requestCompleteOrFailed = true
    );
  }

  createProject() {
    // Implement project creation logic
    const dialogRef = this.dialog.open(ProjectCreateDialogComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.getProjectList();
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
    // Implement status update logic
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
