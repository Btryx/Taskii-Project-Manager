import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ProjectService } from '../project.service';
import { Project } from '../project';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class ProjectCreateDialogComponent {
  project: Project = {
    projectId: '',
    projectName: '',
    createdAt: new Date(),
    active: true,
    parentId: '',
    userId: ''
  };

  isLoading = false;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<ProjectCreateDialogComponent>,
    private projectService: ProjectService
  ) {}

  onSubmit() {
    if (!this.project.projectName) {
      this.errorMessage = 'Project name is required';
      return;
    }

    this.isLoading = true;
    this.projectService.createProject(this.project).subscribe(
      () => {
        this.dialogRef.close(true);
      },
      error => {
        this.errorMessage = 'Failed to create project. Please try again.';
        this.isLoading = false;
      }
    );
  }

  onCancel() {
    this.dialogRef.close();
  }
}
