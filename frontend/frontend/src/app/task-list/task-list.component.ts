import { ProjectService } from './../project.service';
import { CommonModule  } from '@angular/common';
import { Project } from '../project';
import { ChangeDetectionStrategy, Component, computed, effect, inject, OnInit, signal, ViewChild, WritableSignal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from '../auth.service';
import { Observable } from 'rxjs';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { InfoPopup } from '../info-popup/info-popup';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from '../task';
import { CdkDrag,  CdkDragDrop,  CdkDropList, CdkDropListGroup,  moveItemInArray,  transferArrayItem,} from '@angular/cdk/drag-drop';
import { TaskDialog } from '../task-dialog/task-dialog';
import { Status } from '../status';
import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css'],
  imports: [MatIconModule, CommonModule, MatProgressSpinnerModule, MatButtonModule,
    MatCardModule, MatInputModule, MatFormFieldModule, CdkDropList, CdkDropListGroup, CdkDrag],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskListComponent implements OnInit {

  private projectService : ProjectService = inject(ProjectService);
  private authService = inject(Auth);
  private dialog: MatDialog = inject(MatDialog);
  private router : Router = inject(Router);
  private route : ActivatedRoute = inject(ActivatedRoute);

  projectId!: string;
  project: WritableSignal<Project | null> = signal(null);
  status?: string;
  priority?: number;

  tasks: WritableSignal<Task[]> = signal([]);
  statuses: WritableSignal<Status[]> = signal([]);

  taskMapByStatus = computed(() => {
    const map = new Map<string, Task[]>();
    for (const status of this.statuses()) {
      const filtered = this.tasks().filter(task => task.taskStatus === status.statusName);
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

  getTasks(projectid : string) {
    this.projectService.getProjectTasks(projectid).subscribe({
      next: (data) => {
        this.tasks.set(data);
        console.log(this.tasks());
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
        console.log(this.statuses());
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

  createTasksWithStatus(status : string) {
    console.log(status)
    let task : Task = new Task();
    task.taskStatus = status;
    console.log(task.taskStatus)
    const dialogRef = this.dialog.open(TaskDialog, {
      data: { task, title: 'Create task', statuses: this.statuses() },
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

}
