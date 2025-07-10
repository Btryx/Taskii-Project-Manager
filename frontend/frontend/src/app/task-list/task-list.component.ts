import { ProjectService } from './../project.service';
import { CommonModule  } from '@angular/common';
import { Project } from '../project';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from '../auth.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from '../task';
import { CdkDrag,  CdkDragDrop,  CdkDropList, CdkDropListGroup,  moveItemInArray,  transferArrayItem,} from '@angular/cdk/drag-drop';
import { TaskDialog } from '../task-dialog/task-dialog';
import { Status } from '../status';
import {MatCardModule} from '@angular/material/card';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select'
import { User } from '../user';
import { CollaboratorService } from '../collaborator.service';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css'],
  imports: [MatIconModule, CommonModule, MatProgressSpinnerModule, MatButtonModule, MatMenu, MatMenuTrigger, MatMenuItem,
    MatCardModule, MatInputModule, MatFormFieldModule, CdkDropList, CdkDropListGroup, CdkDrag, FormsModule, MatSelectModule, MatTooltipModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskListComponent implements OnInit {

  private projectService : ProjectService = inject(ProjectService);
  private authService = inject(Auth);
  private collaboratorService = inject(CollaboratorService);
  private dialog: MatDialog = inject(MatDialog);
  private router : Router = inject(Router);
  private route : ActivatedRoute = inject(ActivatedRoute);

  projectId!: string;
  project: WritableSignal<Project | null> = signal(null);
  status?: string;
  priority?: number;
  owner: WritableSignal<string> = signal('');
  collaborators: WritableSignal<User[]> = signal([]);

  searchValue?: WritableSignal<string> = signal('');
  priorityFilterValue?: WritableSignal<number> = signal(0);

  tasks: WritableSignal<Task[]> = signal([]);

  statuses: WritableSignal<Status[]> = signal([]);
  priorities = ["Lowest", "Low", "Medium", "High", "Highest"];

  taskMapByStatus = computed(() => {
    const map = new Map<string, Task[]>();
    for (const status of this.statuses()) {
      const filtered = this.tasks()
        .filter(task => this.filterByStatus(task, status))
        .filter(t => this.filterBySearchBarValue(t))
        .filter(t => this.filterByPriorityFilter(t));
      map.set(status.statusName, filtered);
    }
    return map;
  });

  usernames = signal(new Map<string, string>);

  errorMessage = signal('');
  isLoading = signal(true);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.projectId = params["projectId"];
      this.status = params['status'];
      this.priority = params['priority']
        ? Number(params['priority'])
        : undefined;

      if (this.projectId) {
        this.projectService.getProjectById(this.projectId).subscribe({
          next: (data) => {
            if(data) {
              this.project.set(data);
              this.getTasks(this.projectId);
              this.getStatuses(this.projectId);
              this.getOwner();
              this.getCollaborators();
            }
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            console.log(error);
          }
        })
      }
    })
  }

  private filterBySearchBarValue(t: Task): unknown {
    return this.searchValue ? t.taskTitle.toLowerCase().includes(this.searchValue().toLowerCase()) : t;
  }

  private filterByStatus(task: Task, status: Status): unknown {
    return task.taskStatus === status.statusName;
  }

  private filterByPriorityFilter(t: Task): unknown {
    return this.priorityFilterValue && this.priorityFilterValue() > 0 ? t.taskPriority === this.priorityFilterValue() : t;
  }

  getTasks(projectid : string) {
    this.projectService.getProjectTasks(projectid).subscribe({
      next: (data) => {
        this.tasks.set(data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.error.message);
        console.log(error);
      },
      complete: () => this.isLoading.set(false)
    });
  }

  getStatuses(projectId: string) {
    this.projectService.getStatuses(projectId).subscribe({
      next: (data) => {
        this.statuses.set(data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.error.message);
        console.log(error);
      },
    })
  }

  drop(event: CdkDragDrop<Task[]>, targetStatus: Status) {
    const prevList = event.previousContainer.data;
    const currList = event.container.data;

    if (event.previousContainer === event.container) {
      moveItemInArray(currList, event.previousIndex, event.currentIndex);
      //TODO: persist order inside kanban column
    } else {
      transferArrayItem(prevList, currList, event.previousIndex, event.currentIndex);

      const movedTask = currList[event.currentIndex];
      movedTask.taskStatus = targetStatus.statusName;

      //trigger Angular Signal to refresh computed map, and update the ui
      this.tasks.update(tasks =>
        tasks.map(t => t.taskId === movedTask.taskId ? { ...t, taskStatus: movedTask.taskStatus } : t)
      );

      //send update to backend
      this.projectService.updateProjectTask(movedTask).subscribe({
        next: () => {console.log('Task moved!');},
        error: (error) => this.errorMessage.set(error.error.message)
      })
    }
  }

  createTasks() {
    const dialogRef = this.dialog.open(TaskDialog, {
      data: { ...new Task(), title: 'Create task', statuses: this.statuses() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        result.projectId = this.projectId;
        this.projectService.createProjectTask(result).subscribe({
          next: data => {
            this.tasks.update(value => [...value, data]);
          },
          error: (error: HttpErrorResponse) => {
            console.error(error);
            this.errorMessage.set(error.error.message);
          },
        })
      }
    });
  }

  createTasksWithStatus(status : string, event : Event) {
    event.stopPropagation();
    console.log(status)
    let task : Task = new Task();
    task.taskStatus = status;
    console.log(task.taskStatus)
    const dialogRef = this.dialog.open(TaskDialog, {
      data: { ...task, title: 'Create task', statuses: this.statuses() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        result.projectId = this.projectId;
        this.projectService.createProjectTask(result).subscribe({
          next: data => {
            this.tasks.update(value => [...value, data]);
          },
          error: (error: HttpErrorResponse) => {
            console.error(error);
            this.errorMessage.set(error.error.message);
          },
        })
      }
    });
  }

  editTask(task : Task, event : Event) {
    event.stopPropagation();
    const dialogRef = this.dialog.open(TaskDialog, {
      data: { ...task, title: 'Edit task', statuses: this.statuses() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.projectService.updateProjectTask(result).subscribe({
          next: () => {
            this.tasks.update(value => {
              let indexOfUpdatedTask = value.findIndex(item => item.taskId === result.taskId);
              value.splice(indexOfUpdatedTask, 1, result);
              return [...value];
            });
          },
          error: (error: HttpErrorResponse) => {
            console.error(error);
            this.errorMessage.set(error.error.message);
          },
        })
      }
    });
  }

  deleteTask(task : Task,  event : Event) {
    event.stopPropagation();
    const dialogRef = this.dialog.open(ConfirmationDialog, {
      disableClose: false
    });
    dialogRef.componentInstance.title = "Delete task?"
    dialogRef.componentInstance.confirmMessage = `Are you sure you want to delete task: "${task.taskTitle}"?
                                                  This process cannot be undone!`
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.projectService.deleteProjectTask(task).subscribe({
          next: () => {
            this.tasks.update(value => {
              let indexOfUpdatedTask = value.findIndex(item => item.taskId === task.taskId);
              value.splice(indexOfUpdatedTask, 1);
              return [...value];
            });
          },
          error: (error: HttpErrorResponse) => {
            console.error(error);
            this.errorMessage.set(error.error.message);
          },
        })
      }
    });
  }

  getOwner()  {
    this.authService.getUserNameById(this.project()?.userId!).subscribe({
      next: (result) => {
        this.owner.set(result.message);
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
        this.errorMessage.set(error.error.message);
        return error;
      },
    })
  }

  getCollaborators() {
    console.log("result")
    this.collaboratorService.getCollaborators(this.projectId).subscribe({
      next: (result) => {
        console.log(result)
        this.collaborators.set(result);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.error.message);
        return error;
      },
    })
  }

  getColorForUser(id: string): string {
    return this.authService.getColorForUser(id);
  }


}
