import { Component, OnInit } from '@angular/core';
import {TaskService} from "../task.service";
import {Task} from "../task";
import {Router} from "@angular/router";
import {EditTaskComponent} from "../edit-task/edit-task.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DeleteTaskComponent} from "../delete-task/delete-task.component";
import {delay} from "rxjs";

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {

  public tasks: Task[];

  constructor(public taskService : TaskService, public router: Router,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getTaskList();
  }

  getTaskList(){
    this.taskService.getAllTasks().subscribe( tasks => {
      this.tasks = tasks;
    })
  }

  openEditTask(task : Task){
    let dialogRef = this.dialog.open(EditTaskComponent, {
      width: '400px',
      height: '430px',
      data:{
        taskId: task.taskId,
        taskTitle: task.taskTitle,
        taskDate: task.taskDate,
        taskStatus: task.taskStatus,
        taskPriority: task.taskPriority,
        taskDesc: task.taskDesc,
      }
    })

    dialogRef.afterClosed().subscribe( result => {
      this.taskService.updateTask(task.taskId, result).subscribe( () => {
        this.ngOnInit();
      })
    })
  }

  deleteTask(task) {
    let dialogRef = this.dialog.open(DeleteTaskComponent, {
      width: '300px',
      height: '200px',
      data:{
        taskId: task.taskId,
      }
    })

    dialogRef.afterClosed().subscribe( result => {
      this.ngOnInit();
    })
  }
}
