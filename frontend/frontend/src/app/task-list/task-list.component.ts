import { map, pipe } from 'rxjs';
import { ProjectService } from '../service/project.service';
import { CommonModule  } from '@angular/common';
import { Project } from '../model/project';
import { ChangeDetectionStrategy, Component, computed, ElementRef, inject, OnInit, signal, ViewChild, WritableSignal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from '../service/auth.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from '../model/task';
import { CdkDrag,  CdkDragDrop,  CdkDropList, CdkDropListGroup,  moveItemInArray,  transferArrayItem,} from '@angular/cdk/drag-drop';
import { TaskDialog } from '../task-dialog/task-dialog';
import { Status } from '../model/status';
import {MatCardModule} from '@angular/material/card';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select'
import { User } from '../model/user';
import { CollaboratorService } from '../service/collaborator.service';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MemberDialog } from '../member-dialog/member-dialog';
import { InfoPopup } from '../info-popup/info-popup';
import { Member } from '../model/member';
import { ManageMembersDialog } from '../../manage-members-dialog/manage-members-dialog';
import { ErrorPopup } from '../error-popup/error-popup';
import { StatusService } from '../service/status.service';

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
  private statusService : StatusService = inject(StatusService);
  private authService = inject(Auth);
  private collaboratorService = inject(CollaboratorService);
  private dialog: MatDialog = inject(MatDialog);
  private route : ActivatedRoute = inject(ActivatedRoute);

  projectId!: string;
  project: WritableSignal<Project | null> = signal(null);
  status?: string;
  priority?: number;
  collaborators: WritableSignal<User[]> = signal([]); //getting it as users for better performance
  members: WritableSignal<Member[]> = signal([]); //getting it as users for better performance
  isAdmin = signal(false);
  createStatusInputVisible = signal(false);
  newStatusName = signal("");

  selectedCollaboratorFilterValue: WritableSignal<string> = signal('');
  searchValue?: WritableSignal<string> = signal('');
  priorityFilterValue?: WritableSignal<number> = signal(0);

  tasks: WritableSignal<Task[]> = signal([]);

  statuses: WritableSignal<Status[]> = signal([]);
  priorities = ["Lowest", "Low", "Medium", "High", "Highest"];

  @ViewChild('statusInput') statusInput!: ElementRef;

  taskMapByStatus = computed(() => {
    const map = new Map<string, Task[]>();
    for (const status of this.statuses()) {
      const filtered = this.tasks()
        .filter(task => this.filterByStatus(task, status))
        .filter(task => this.filterBySearchBarValue(task))
        .filter(task => this.filterByPriorityFilter(task))
        .filter(task => this.filterBySelectedCollaboratorValue(task))
        .sort((a: Task, b: Task) => {
          if(b.orderNumber == null) b.orderNumber = 0;
          if(a.orderNumber == null) a.orderNumber = 0;
          return a.orderNumber - b.orderNumber
        })
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
              this.getCollaborators();
              this.getTasks(this.projectId);
              this.getStatuses(this.projectId);
              this.isUserAdmin();
            }
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            console.log(error);
          },
          complete: () => {
            setTimeout(() => {this.isLoading.set(false)}, 1000)
            this.getMembers(); // dont need to wait for this
          }
        })
      }
    })
  }

  isUserAdmin() {
    this.projectService.isAdmin(this.projectId).subscribe({
      next: (data : boolean) => {
        this.isAdmin.set(data);
      }
    })
  }

  toggleCreateStatusInputVisible() {
    this.createStatusInputVisible.set(!this.createStatusInputVisible());
    this.newStatusName.set("");

    if(this.createStatusInputVisible()) {
      setTimeout(() => {
        this.statusInput.nativeElement.focus()
      }, 200)
    }
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

  private filterBySelectedCollaboratorValue(t: Task): unknown {
    if(this.selectedCollaboratorFilterValue() === '') {
      return t; //no filter
    }
    if(this.selectedCollaboratorFilterValue() === 'Unassigned') {
      return t.assignee === null|| t.assignee === '';
    }
    return this.selectedCollaboratorFilterValue() === t.assignee;
  }

  selectCollaboratorFilter(filterValue: string) {
    if(this.selectedCollaboratorFilterValue() === filterValue) {
      this.selectedCollaboratorFilterValue.set(''); //unselect
      return;
    }
    this.selectedCollaboratorFilterValue.set(filterValue);
  }

  getTasks(projectid : string) {
    this.projectService.getProjectTasks(projectid)
    .pipe(map((taskArray) => taskArray.map((task: Task) => {
      this.setAssigneeUsernameMap(task);
      return task;
    })))
    .subscribe({
      next: (data) => {
        console.log(data)
        this.tasks.set(data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.error.message);
        console.log(error);
      }
    });
  }

  getStatuses(projectId: string) {
    this.statusService.getStatuses(projectId).subscribe({
      next: (data) => {
        this.statuses.set(data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.error.message);
        console.log(error);
      },
    })
  }

  updateStatus(status: Status, event: Event) {
    let name = (event.target as HTMLInputElement).value;
    if (name) {
      if (!this.checkIfNameUnique(name)) {
        const dialogRef = this.dialog.open(InfoPopup, {
          disableClose: false
        });
        dialogRef.componentInstance.title = "This name is already being used in this project.";
        (event.target as HTMLInputElement).value = status.statusName;
        return;
      }
      status.statusName = name.toUpperCase();
      console.log(status)
      this.statusService.updateStatus(status).subscribe({
        next: (data) => {
          this.isLoading.set(true);
          this.getTasks(this.projectId);
          this.statuses.update(value => {
            let indexOfUpdatedStatus = value.findIndex(item => item.statusId === data.statusId);
            value.splice(indexOfUpdatedStatus, 1, data);
            return [...value];
          })
          if (event) {
            (event.target as HTMLInputElement).blur();
          }
        },
        error: (error: HttpErrorResponse) => {
          (event.target as HTMLInputElement).value = status.statusName;
          this.errorMessage.set(error.error.message);
          const dialogRef = this.dialog.open(ErrorPopup, {
            disableClose: false
          });
          dialogRef.componentInstance.title = "Error renaming column!"
          dialogRef.componentInstance.errorMessage = error.error.message;
          this.getStatuses(this.projectId);
        },
        complete: () => setTimeout(() => {this.isLoading.set(false)}, 1000)
      })
    }
    setTimeout(() => {this.isLoading.set(false)}, 1000)
  }

  createStatus() {
    if (this.newStatusName()) {
      if (!this.checkIfNameUnique(this.newStatusName())) {
          const dialogRef = this.dialog.open(InfoPopup, {
            disableClose: false
          });
          dialogRef.componentInstance.title = "This name is already being used in this project."
          this.newStatusName.set("");
      } else {
        let status: Status = new Status();
        status.statusName = this.newStatusName();
        console.log(this.statuses().length + 1);
        status.orderNumber  = this.statuses().length + 1;
        status.projectId = this.projectId;
        this.statusService.createStatus(status).subscribe({
          next: (data) => {
            this.statuses.update(value => {
              return [...value, data];
            })
            this.createStatusInputVisible.set(false);
            this.newStatusName.set("");
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            const dialogRef = this.dialog.open(ErrorPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Error creating column!"
            dialogRef.componentInstance.errorMessage = error.error.message;
            this.newStatusName.set("");
          },
        })
      }
    }
  }


  checkIfNameUnique(name: string) {
    return !this.statuses().map(s=> s.statusName.toLowerCase()).includes(name.toLowerCase());;
  }

  deleteStatus(status: Status, event: Event) {
    this.statusService.deleteStatus(status.statusId).subscribe({
      next: () => {
        this.statuses.update(value => {
          let indexOfUpdatedStatus = value.findIndex(item => item.statusId === status.statusId);
          value.splice(indexOfUpdatedStatus, 1);
          return [...value];
        })
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.error.message);
        const dialogRef = this.dialog.open(ErrorPopup, {
          disableClose: false
        });
        dialogRef.componentInstance.title = "Error deleting column!"
        dialogRef.componentInstance.errorMessage = error.error.message;
      },
    })
  }

  drop(event: CdkDragDrop<Task[]>, targetStatus: Status) {
    const prevList = event.previousContainer.data;
    const currList = event.container.data;

    if (event.previousContainer === event.container) {
      moveItemInArray(currList, event.previousIndex, event.currentIndex);
      currList.forEach((task, index) => {
        task.orderNumber = index;
        this.projectService.updateProjectTask(task).subscribe({})
      });
    } else {
      transferArrayItem(prevList, currList, event.previousIndex, event.currentIndex);

      const movedTask = currList[event.currentIndex];
      movedTask.taskStatus = targetStatus.statusName;

      currList.forEach((task, index) => {
        task.orderNumber = index;
        this.projectService.updateProjectTask(task).subscribe({})
      });
    }
  }

  createTasks() {
    const dialogRef = this.dialog.open(TaskDialog, {
      data: { ...new Task(), title: 'Create task', statuses: this.statuses(), collaborators: this.collaborators() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        result.projectId = this.projectId;
        this.projectService.createProjectTask(result).subscribe({
          next: data => {
            this.setAssigneeUsernameMap(data);
            this.tasks.update(value => [...value, data]);
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            const dialogRef = this.dialog.open(ErrorPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Error creating task!"
            dialogRef.componentInstance.errorMessage = error.error.message;
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
      data: { ...task, title: 'Create task', statuses: this.statuses(), collaborators: this.collaborators() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        result.projectId = this.projectId;
        this.projectService.createProjectTask(result).subscribe({
          next: data => {
            this.setAssigneeUsernameMap(data);
            this.tasks.update(value => [...value, data]);
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            const dialogRef = this.dialog.open(ErrorPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Error creating task!"
            dialogRef.componentInstance.errorMessage = error.error.message;
          },
        })
      }
    });
  }

  editTask(task : Task, event : Event) {
    event.stopPropagation();
    const dialogRef = this.dialog.open(TaskDialog, {
      data: { ...task, title: 'Edit task', statuses: this.statuses(), collaborators: this.collaborators()  },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.projectService.updateProjectTask(result).subscribe({
          next: () => {
            this.tasks.update(value => {
              this.setAssigneeUsernameMap(result);
              let indexOfUpdatedTask = value.findIndex(item => item.taskId === result.taskId);
              value.splice(indexOfUpdatedTask, 1, result);
              return [...value];
            });
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            const dialogRef = this.dialog.open(ErrorPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Error updating task!"
            dialogRef.componentInstance.errorMessage = error.error.message;
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

  getCollaborators() {
    console.log("result")
    this.collaboratorService.getCollaboratorsAsUsers(this.projectId)
      .subscribe({
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

  getMembers() {
    console.log("result")
    this.collaboratorService.getCollaborators(this.projectId)
      .subscribe({
        next: (result) => {
          console.log(result)
          console.log(result)
          this.members.set(result)
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage.set(error.error.message);
        },
      })
  }

  createMember() {
    let member = new Member();
    member.projectId = this.projectId;
    const dialogRef = this.dialog.open(MemberDialog, {
      data: { ...member, collaborators: this.collaborators() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.collaboratorService.createCollaborator(result).subscribe({
          next: data => {
            const dialogRef = this.dialog.open(InfoPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Successfully added member to project!"
            this.getCollaborators();
            this.getMembers();
          },
          error: (error: HttpErrorResponse) => {
            this.errorMessage.set(error.error.message);
            const dialogRef = this.dialog.open(ErrorPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Error adding member to project!"
            dialogRef.componentInstance.errorMessage = error.error.message;
          },
        })
      }
    });
  }

  manageMembers() {
    const dialogRef = this.dialog.open(ManageMembersDialog, {
      data: { collaborators: this.members() },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getCollaborators();
      this.members.set(result);
      this.getTasks(this.projectId);
    });
  }

  setAssigneeUsernameMap(task: Task): void {
    if(task.assignee &&!this.usernames().has(task.assignee)) {
      this.authService.getUserNameById(task.assignee).subscribe({
        next: (data) => {
          this.usernames.update(map => {
            const newMap = new Map(map);
            newMap.set(task.assignee!, data.message);
            return newMap;
          })
        },
        error: () => {
          this.usernames.update(map => {
            const newMap = new Map(map);
            newMap.set(task.assignee!, "");
            return newMap;
          })
        }
      });
    }
  }

  getAssigneeNameForTask(task: Task) {
    return task.assignee ? this.usernames().get(task.assignee) || '' : '';
  }

  getColorForUser(id: string): string {
    return this.authService.getColorForUser(id);
  }


}
