import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from './project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  projectUrl : string = 'http://localhost:8082/api/projects';

  constructor(
    private http : HttpClient
  ) {}

  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.projectUrl + "/all")
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

}
