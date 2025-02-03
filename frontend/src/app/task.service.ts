import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, Subject, tap } from "rxjs";
import { Task } from "./task";
import { AuthService } from "./login/auth.service";
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private URL = `${environment.apiBaseUrl}/api/tasks`;
  private URLAll = `${environment.apiBaseUrl}/api/tasks/all`;

  username: string = "";
  password: string = "";

  // Subjects for task events
  private taskCreatedSource = new Subject<void>();
  private taskUpdatedSource = new Subject<void>();
  private taskDeletedSource = new Subject<void>();

  // Observable streams
  taskCreated$ = this.taskCreatedSource.asObservable();
  taskUpdated$ = this.taskUpdatedSource.asObservable();
  taskDeleted$ = this.taskDeletedSource.asObservable();

  constructor(private httpClient: HttpClient, private authService: AuthService) {}

  getTaskList(projectId: string, status?: string, priority?: number): Observable<Task[]> {
    let params = new HttpParams().set('projectId', projectId);
    if (status) params = params.set('status', status);
    if (priority !== undefined) params = params.set('priority', priority.toString());

    return this.httpClient.get<Task[]>(`${this.URL}/filter`, { headers: this.getHeaders(), params });
  }

  createTask(task: Task): Observable<Task> {
    return this.httpClient.post<Task>(this.URLAll, task, { headers: this.getHeaders() }).pipe(
      tap(() => {
        console.log('Task created:', task.taskTitle);
        this.taskCreatedSource.next();
      })
    );
  }

  updateTask(task: Task): Observable<Object> {
    return this.httpClient.put(`${this.URL}/${task.taskId}`, task, { headers: this.getHeaders() }).pipe(
      tap(() => {
        console.log('Task updated:', task.taskTitle);
        this.taskUpdatedSource.next();
      })
    );
  }

  deleteTask(id: string): Observable<Object> {
    return this.httpClient.delete(`${this.URL}/${id}`, { headers: this.getHeaders() }).pipe(
      tap(() => {
        console.log('Task deleted:', id);
        this.taskDeletedSource.next();
      })
    );
  }

  private getHeaders() {
    this.username = this.authService.getLoggedInUserName();
    this.password = this.authService.getLoggedInPassword();
    return new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
  }
}
