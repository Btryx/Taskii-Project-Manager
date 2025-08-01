import { HttpClient } from '@angular/common/http';
import { Injectable, Inject, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from '../models/project';
import { Task } from '../models/task';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  projectUrl : string = 'http://localhost:8082/api/projects';
  taskUrl : string = 'http://localhost:8082/api/tasks';

  private http: HttpClient = inject(HttpClient);

  getAllProjects(): Observable<any[]> {
    return this.http.get<any>(this.projectUrl + "/all")
  }

  getProjectById(projectId: string): Observable<any> {
    return this.http.get<any>(`${this.projectUrl}/${projectId}`);
  }

  createProject(project: Project): Observable<Project> {
    return this.http.post<Project>(this.projectUrl + "/all", project);
  }

  updateProject(project: Project, id: string): Observable<Project> {
    return this.http.put<Project>(`${this.projectUrl}/${id}`, project);
  }

  deleteProject(projectId: string) : Observable<Object> {
    return this.http.delete<Project>(`${this.projectUrl}/${projectId}`);
  }

  getProjectTasks(projectId: string) : Observable<any> {
    return this.http.get<any>(`${this.taskUrl}/filter`, {params: {'projectId': projectId}})
  }

  createProjectTask(task: Task) : Observable<any> {
    return this.http.post<any>(`${this.taskUrl}/all`, task);
  }

  updateProjectTask(task: Task) : Observable<any> {
    return this.http.put(`${this.taskUrl}/${task.taskId}`, task);
  }

  deleteProjectTask(task: Task) : Observable<any> {
    return this.http.delete(`${this.taskUrl}/${task.taskId}`);
  }

  isAdmin(projectId: string) : Observable<boolean> {
    return this.http.get<boolean>(`${this.projectUrl}/admin/${projectId}`);
  }

}
