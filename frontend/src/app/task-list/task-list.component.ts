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

  projectId!: string;
  status?: string;
  priority?: number;

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
    this.route.queryParams.subscribe(params => {
      this.projectId = params['projectId']; // Required
      this.status = params['status']; // Optional
      this.priority = params['priority'] ? Number(params['priority']) : undefined; // Optional

      if (this.projectId) {
        this.getFilteredTasks();
      }
    });
  }

  getFilteredTasks(): void {
    console.log('Current status:', this.status);

    if (!this.authService.isUserLoggedIn()) {
      this.errorType = 'auth';
      this.errorMessage = 'Please log in to view your tasks';
      this.requestCompleteOrFailed = true;
      return;
    }

    this.taskService.getTaskList(this.projectId, this.status, this.priority).subscribe(
      data => {
      this.tasks = data;
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

  openEditTask(task: Task) {
    const dialogRef = this.dialog.open(EditTaskComponent, {
      width: '500px',
      data: {
        ...task,
        title: 'Edit Task'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.updateTask(task.taskId, result).subscribe({
          next: () => {
            this.getFilteredTasks();
          },
          error: (error) => {
            console.error('Error updating task:', error);
          }
        });
      }
    });
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
