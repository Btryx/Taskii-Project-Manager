import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskListComponent} from "./task-list/task-list.component";
import {CreateTaskComponent} from "./create-task/create-task.component";
import {EditTaskComponent} from "./edit-task/edit-task.component";
import {TodoTasksComponent} from "./todo-tasks/todo-tasks.component";
import {FinishedTasksComponent} from "./finished-tasks/finished-tasks.component";
import {DueTasksComponent} from "./due-tasks/due-tasks.component";
import {LoginComponent} from "./login/login.component";

const routes: Routes = [
  { path: 'tasks', component: TaskListComponent },
  { path: 'create-task', component: CreateTaskComponent },
  { path: 'edit-task', component: EditTaskComponent },
  { path: 'todo-tasks', component: TodoTasksComponent },
  { path: 'finished-tasks', component: FinishedTasksComponent },
  { path: 'due-tasks', component: DueTasksComponent },
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'tasks', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
