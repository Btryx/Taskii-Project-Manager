import {Component, OnInit} from '@angular/core';
import {TaskService} from "./task.service";
import {NavigationEnd, Router} from "@angular/router";
import {AuthService} from "./login/auth.service";
import { MatSelectChange } from '@angular/material/select';
import { TaskStatus } from './task-status.enum';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'To-do Notes';
  readonly TaskStatus = TaskStatus;
  currentPage: string = 'Tasks';

  constructor(public router: Router, public loginService: AuthService) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      // Update current page based on URL
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

  ngOnInit(): void {
  }

  openAddTask(){
    this.router.navigate(['create-task']);
  }

  toProjects(){
    this.router.navigate(['projects']);
  }

  onFilterChange(event: MatSelectChange) {
    this.router.navigate(['/tasks', event.value]);
  }
}
