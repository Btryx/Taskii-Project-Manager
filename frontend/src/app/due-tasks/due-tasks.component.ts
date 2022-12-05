import { Component, OnInit } from '@angular/core';
import {Task} from "../task";
import {TaskService} from "../task.service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {EditTaskComponent} from "../edit-task/edit-task.component";
import {DeleteTaskComponent} from "../delete-task/delete-task.component";

@Component({
  selector: 'app-todo-tasks',
  templateUrl: './due-tasks.component.html',
  styleUrls: ['./due-tasks.component.css']
})
export class DueTasksComponent implements OnInit {

  public tasks: Task[];
  public requestCompleteOrFailed: boolean;

  constructor(public taskService : TaskService, public router: Router,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.requestCompleteOrFailed = false;
    this.getTaskList();
  }

  getTaskList(){
    this.taskService.getAllTasks().subscribe( tasks => {
        this.tasks = tasks;
      },
      () => this.requestCompleteOrFailed = true,
      () => this.requestCompleteOrFailed = true)
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
