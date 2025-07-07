import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { ProjectListComponent } from './project-list/project-list.component';
import { TaskListComponent } from './task-list/task-list.component';


export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'projects', component: ProjectListComponent},
  { path: 'tasks', component: TaskListComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
 // { path: '**', component: PageNotFoundComponent },
];
