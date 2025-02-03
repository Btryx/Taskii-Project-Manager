import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject, tap } from 'rxjs';
import { Project } from './project';
import { environment } from 'src/environments/environment';
import { AuthService } from './login/auth.service';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private URL = `${environment.apiBaseUrl}/api/projects`;
  private URLAll = `${environment.apiBaseUrl}/api/projects/all`;

  username: string = '';
  password: string = '';

    // Subjects for project events
    private projectCreatedSource = new Subject<void>();
    private projectUpdatedSource = new Subject<void>();
    private projectDeletedSource = new Subject<void>();

    // Observable streams
    projectCreated$ = this.projectCreatedSource.asObservable();
    projectUpdated$ = this.projectUpdatedSource.asObservable();
    projectDeleted$ = this.projectDeletedSource.asObservable();

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService
  ) {}

  getAllProjects(): Observable<Project[]> {
    const headers = this.getHeaders();
    return this.httpClient.get<Project[]>(this.URLAll, { headers });
  }

  getProject(id: string): Observable<Project> {
    const headers = this.getHeaders();
    return this.httpClient.get<Project>(`${this.URL}/${id}`, { headers });
  }

  createProject(project: Project): Observable<Project> {
    const headers = this.getHeaders();
    return this.httpClient.post<Project>(this.URLAll, project, { headers }).pipe(
      tap(() => {
        console.log('Project created:', project.projectName);
        this.projectCreatedSource.next();
      })
    );
  }

  updateProject(id: string, project: Project): Observable<Object> {
    const headers = this.getHeaders();
    return this.httpClient.put(`${this.URL}/${id}`, project, { headers });
  }

  deleteProject(id: string): Observable<Object> {
    const headers = this.getHeaders();
    return this.httpClient.delete(`${this.URL}/${id}`, { headers });
  }

  private getHeaders() {
    this.username = this.authService.getLoggedInUserName();
    this.password = this.authService.getLoggedInPassword();
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(this.username + ':' + this.password),
    });
    return headers;
  }
}
