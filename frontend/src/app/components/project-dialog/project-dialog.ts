import { Component, Inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Project } from '../../models/project';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select'
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';

interface ProjectDialogData extends Project {
  title: string;
}

@Component({
  selector: 'app-project-dialog',
  imports: [MatFormFieldModule, MatSelectModule, ReactiveFormsModule, MatInputModule, MatButtonModule],
  templateUrl: './project-dialog.html',
  styleUrl: './project-dialog.css'
})
export class ProjectDialog implements OnInit {
  projectForm = new FormGroup({
    projectId: new FormControl(''),
    projectName: new FormControl('', Validators.required),
    projectDesc: new FormControl(''),
    createdAt: new FormControl(),
    active: new FormControl(false, Validators.required),
    parentId: new FormControl(''),
    userId: new FormControl(''),
  })

  constructor(
    private dialogRef: MatDialogRef<ProjectDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ProjectDialogData
  ) {
  }

  ngOnInit(): void {
    this.projectForm.patchValue({
      projectId: this.data.projectId,
      projectName: this.data.projectName,
      projectDesc: this.data.projectDesc,
      createdAt: this.data.createdAt,
      active: this.data.active,
      parentId: this.data.parentId,
      userId: this.data.userId,
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
