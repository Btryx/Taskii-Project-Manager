import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import { Project } from './project';
import { environment } from 'src/environments/environment';
import { AuthService } from './login/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private URL = `${environment.apiBaseUrl}/api/projects`;
  private URLAll = `${environment.apiBaseUrl}/api/projects/all`;

  username: string = "";
  password: string = "";

  constructor(private httpClient: HttpClient, private authService: AuthService) { }

    getAllProjects(): Observable<Project[]>{
      const headers = this.getHeaders();
      return this.httpClient.get<Project[]>(this.URLAll, {headers});
    }

    getProject(id: string): Observable<Object>{
      const headers = this.getHeaders();
      return this.httpClient.get(`${this.URL}/${id}`,{headers});
    }

    createProject(project: Project): Observable<Object>{
      const headers = this.getHeaders();
      return this.httpClient.post(this.URLAll, project, {headers});
    }

    updateProject(id: string, project: Project): Observable<Object>{
      const headers = this.getHeaders();
      return this.httpClient.put(`${this.URL}/${id}`, project, {headers});
    }

    deleteProject(id: string): Observable<Object>{
      const headers = this.getHeaders();
      return this.httpClient.delete(`${this.URL}/${id}`,{headers})
    }

  private getHeaders() {
    this.username = this.authService.getLoggedInUserName();
    this.password = this.authService.getLoggedInPassword();
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
    return headers;
  }
}
