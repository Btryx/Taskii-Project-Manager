import { Component, OnInit } from '@angular/core';
import { TaskService } from './task.service';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { AuthService } from './login/auth.service';
import { MatSelectChange } from '@angular/material/select';
import { TaskStatus } from './task-status.enum';
import { filter } from 'rxjs';
import { Task } from './task';
import { EditTaskComponent } from './edit-task/edit-task.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'To-do Notes';
  readonly TaskStatus = TaskStatus;
  currentPage: string = 'Projects';

  constructor(
    public router: Router,
    public loginService: AuthService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    public taskService: TaskService
  ) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url.includes('/projects')) {
          this.currentPage = 'Projects';
        } else if (event.url.includes('/tasks')) {
          this.currentPage = 'Tasks';
        } else if (event.url.includes('/create-task')) {
          this.currentPage = 'Create Task';
        } else {
          this.currentPage = 'Your Tasks';
        }
      });
  }

  ngOnInit(): void {}

  openCreateTask() {
    var task = new Task();
    const dialogRef = this.dialog.open(EditTaskComponent, {
      width: '500px',
      data: {
        ...task,
        title: 'Create Task',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        const projectId = this.route.snapshot.queryParamMap.get('projectId');
        const status = this.route.snapshot.queryParamMap.get('status');
        if (projectId) {
          result.projectId = projectId;
          this.taskService.createTask(result).subscribe({
            next: () => {
              console.log('Task successfully created!');
            },
            error: (error) => {
              console.error('Error creating task:', error);
            },
          });
        } else {
          console.error('Invalid project id!');
        }
      }
    });
  }

  toProjects() {
    this.router.navigate(['projects']);
  }

  onFilterChange(event: MatSelectChange) {
    if (this.currentPage === 'Tasks') {
      const projectId = this.route.snapshot.queryParamMap.get('projectId');

      if (projectId) {
        this.navigateToFilteredTasksPage(event.value, projectId);
      }
    }
  }

  navigateToFilteredTasksPage(status: string, projectId: string) {
    this.router.navigate(['tasks/filter'], {
      queryParams: {
        projectId: projectId,
        status: status,
        priority: null,
      },
      queryParamsHandling: 'merge',
    });
  }
}
