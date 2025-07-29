import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { TaskListComponent } from './components/task-list/task-list.component';


export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'projects', component: ProjectListComponent},
  { path: 'tasks', component: TaskListComponent },
  { path: '', redirectTo: 'projects', pathMatch: 'full' },
 // { path: '**', component: PageNotFoundComponent },
];
