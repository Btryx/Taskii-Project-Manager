import { Component, OnInit } from '@angular/core';
import {TaskService} from "../task.service";
import {Task} from "../task";
import {ActivatedRoute, Router} from "@angular/router";
import {EditTaskComponent} from "../edit-task/edit-task.component";
import {MatDialog} from "@angular/material/dialog";
import {DeleteTaskComponent} from "../delete-task/delete-task.component";
import { AuthService } from '../login/auth.service';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { TaskStatus } from '../task-status.enum';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  public tasks: Task[];
  public todoTasks: Task[] = [];
  public inProgressTasks: Task[] = [];
  public finishedTasks: Task[] = [];
  public dueTasks: Task[] = [];

  public status: string;
  public requestCompleteOrFailed: boolean;
  public errorMessage: string = '';
  public errorType: 'auth' | 'loading' | null = null;

  constructor(public taskService : TaskService,
              public router: Router,
              private dialog: MatDialog,
              public authService: AuthService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.requestCompleteOrFailed = false;
    this.route.params.subscribe(params => {
      this.status = params['status'] || 'ALL';
      this.getTaskList();
    });
  }

  getTaskList() {
    console.log('Current status:', this.status);

    if (!this.authService.isUserLoggedIn()) {
      this.errorType = 'auth';
      this.errorMessage = 'Please log in to view your tasks';
      this.requestCompleteOrFailed = true;
      return;
    }

    const handleError = (error: any) => {
      this.errorType = 'loading';
      this.requestCompleteOrFailed = true;

      if (error.status === 401) {
        this.errorMessage = 'Your session has expired. Please log in again.';
        this.router.navigate(['/login']);
      } else if (error.status === 400) {
        this.errorMessage = 'Invalid request. Please try again.';
      } else if (error.status === 404) {
        this.errorMessage = 'No tasks found for the selected filter.';
      } else {
        this.errorMessage = 'Unable to load tasks. Please try again later.';
      }
      console.error('Error:', error);
    };

    if (this.status && this.status !== 'ALL') {
      this.taskService.getTasksByStatus(this.status).subscribe(
        tasks => {
          this.tasks = tasks;
          this.sortTasksByStatus();
          this.errorType = null;
          this.errorMessage = '';
        },
        error => {
          console.error('Error:', error);
          this.requestCompleteOrFailed = true;
        },
        () => this.requestCompleteOrFailed = true
      );
    } else {
      this.taskService.getAllTasks().subscribe(
        tasks => {
          this.tasks = tasks;
          this.sortTasksByStatus();
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

  sortTasksByStatus() {
    this.todoTasks = this.tasks.filter(task => task.taskStatus === TaskStatus.TODO);
    this.inProgressTasks = this.tasks.filter(task => task.taskStatus === TaskStatus.IN_PROGRESS);
    this.finishedTasks = this.tasks.filter(task => task.taskStatus === TaskStatus.FINISHED);
    this.dueTasks = this.tasks.filter(task => task.taskStatus === TaskStatus.DUE);
  }

  drop(event: CdkDragDrop<Task[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );

      // Update the task status based on the new container
      const task = event.container.data[event.currentIndex];
      const newStatus : TaskStatus = this.getStatusFromContainerId(event.container.id);
      task.taskStatus = newStatus

      // Update in backend
      this.taskService.updateTask(task.taskId, task).subscribe(
        () => {
          // Task updated successfully
        },
        error => {
          // Handle error and potentially revert the move
          console.error('Error updating task:', error);
          transferArrayItem(
            event.container.data,
            event.previousContainer.data,
            event.currentIndex,
            event.previousIndex
          );
        }
      );
    }
  }

  getStatusFromContainerId(containerId: string): TaskStatus {
    switch (containerId) {
      case 'todo-list': return TaskStatus.TODO;
      case 'finished-list': return TaskStatus.FINISHED;
      case 'due-list': return TaskStatus.DUE;
      case 'inProgress-list': return TaskStatus.IN_PROGRESS;
      default: return TaskStatus.TODO;
    }
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
