import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskListComponent} from "./task-list/task-list.component";
import {CreateTaskComponent} from "./create-task/create-task.component";
import {EditTaskComponent} from "./edit-task/edit-task.component";
import {LoginComponent} from "./login/login.component";
import {LogoutComponent} from "./logout/logout.component";
import { ProjectListComponent } from './project-list/project-list.component';

const routes: Routes = [
  { path: 'tasks/:status', component: TaskListComponent },
  { path: 'create-task', component: CreateTaskComponent },
  { path: 'edit-task', component: EditTaskComponent },
  { path: 'projects', component: ProjectListComponent },
  { path: "login",component: LoginComponent},
  { path: "logout",component: LogoutComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
