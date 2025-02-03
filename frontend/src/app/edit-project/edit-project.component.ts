import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Project } from '../project';

interface ProjectDialogData extends Project {
  title: string;
}

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.css'],
})
export class ProjectEditDialogComponent implements OnInit {
  projectForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ProjectEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ProjectDialogData
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.projectForm.patchValue({
      projectId: this.data.projectId,
      projectName: this.data.projectName,
      createdAt: this.data.createdAt,
      active: this.data.active,
      parentId: this.data.parentId,
      userId: this.data.userId,
    });
  }

  private initializeForm(): void {
    this.projectForm = this.fb.group({
      projectId: [''],
      projectName: ['', [Validators.required, Validators.maxLength(100)]],
      createdAt: [''],
      active: [false, Validators.required],
      parentId: [''],
      userId: [''],
    });
  }

  onSubmit(): void {
    if (this.projectForm.valid) {
      this.dialogRef.close(this.projectForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
