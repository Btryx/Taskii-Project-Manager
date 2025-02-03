import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Task } from '../task';
import { TaskStatus } from '../task-status.enum';

interface TaskDialogData extends Task {
  title: string;
}

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrls: ['./edit-task.component.css']
})
export class EditTaskComponent implements OnInit {
  taskForm: FormGroup;
  taskStatuses = Object.values(TaskStatus);
  priorities = [1, 2, 3, 4, 5];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditTaskComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TaskDialogData
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.taskForm.patchValue({
      taskId: this.data.taskId,
      taskTitle: this.data.taskTitle,
      taskDate: this.data.taskDate,
      taskDesc: this.data.taskDesc,
      taskStatus: this.data.taskStatus,
      taskPriority: this.data.taskPriority,
      projectId: this.data.projectId
    });
  }

  private initializeForm(): void {
    this.taskForm = this.fb.group({
      taskId: [''],
      taskTitle: ['', [Validators.required, Validators.maxLength(100)]],
      taskStatus: ['', Validators.required],
      taskPriority: ['', Validators.required],
      taskDate: ['', Validators.required],
      taskDesc: ['', [Validators.required, Validators.maxLength(300)]],
      projectId: ['']
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
