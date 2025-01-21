import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {TaskService} from "../task.service";
import {Task} from "../task";
import {Router} from "@angular/router";
import { TaskStatus } from './../task-status.enum';

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrls: ['./edit-task.component.css']
})
export class EditTaskComponent implements OnInit {

  public taskId: string;
  public taskPriority: number;
  public taskStatus: string;
  public taskDesc: string;
  public taskDate: Date;
  public taskTitle: string;
  TaskStatus = TaskStatus;

  constructor(private taskService: TaskService, private router: Router,
              public dialogRef: MatDialogRef<EditTaskComponent>, @Inject(MAT_DIALOG_DATA) public data: Task) { }

  ngOnInit(): void {
    this.taskId = this.data.taskId;
    this.taskTitle = this.data.taskTitle;
    this.taskDate = this.data.taskDate;
    this.taskDesc = this.data.taskDesc;
    this.taskStatus = this.data.taskStatus;
    this.taskPriority = this.data.taskPriority;
  }

  save(){
    this.dialogRef.close(
      {
        taskId: this.taskId,
        taskTitle: this.taskTitle,
        taskStatus: this.taskStatus,
        taskPriority: this.taskPriority,
        taskDate: this.taskDate,
        taskDesc: this.taskDesc
      }
    )
  }
}
