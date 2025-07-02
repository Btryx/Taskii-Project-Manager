import { Component, Inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select'
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import { Task } from '../task';
import { MatNativeDateModule } from '@angular/material/core';
import { Status } from '../status';

interface TaskDialogData extends Task {
  title: string;
  statuses: Status[];
}

@Component({
  selector: 'app-task-dialog',
  imports: [MatFormFieldModule, MatSelectModule, ReactiveFormsModule, MatInputModule, MatButtonModule, MatDatepickerModule, MatNativeDateModule],
  templateUrl: './task-dialog.html',
  styleUrl: './task-dialog.css'
})
export class TaskDialog {
  taskForm = new FormGroup({
    taskId: new FormControl(''),
    taskTitle: new FormControl('', Validators.required),
    taskDate: new FormControl(),
    taskStatus: new FormControl('', Validators.required),
    taskPriority: new FormControl(),
    taskDesc: new FormControl(''),
    projectId: new FormControl(''),
  })
  priorities = [1, 2, 3, 4, 5];

  constructor(
    private dialogRef: MatDialogRef<TaskDialog>,
    @Inject(MAT_DIALOG_DATA) public data: TaskDialogData
  ) {
  }

  ngOnInit(): void {
    this.taskForm.patchValue({
      taskId: this.data.taskId,
      taskStatus: this.data.taskStatus,
      taskTitle: this.data.taskTitle,
      taskPriority: this.data.taskPriority,
      taskDate: this.data.taskDate,
      taskDesc: this.data.taskDesc,
      projectId: this.data.projectId
    });
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      this.dialogRef.close(this.taskForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
