import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Task} from "./task";
import {AuthService} from "./login/auth.service";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private URL = "http://localhost:8082/api/tasks";
  private URLAll = "http://localhost:8082/api/tasks/all"

  username: string = "";
  password: string = "";

  constructor(private httpClient: HttpClient, private authService: AuthService) { }

  getAllTasks(): Observable<Task[]>{
    this.username = this.authService.getLoggedInUserName();
    this.password = "kutya";
    console.log(this.username)
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
    return this.httpClient.get<Task[]>(this.URLAll, {headers});
  }

  getTask(id: string): Observable<Object>{
    this.username = this.authService.getLoggedInUserName();
    this.password = "kutya";
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
    return this.httpClient.get(`${this.URL}/${id}`,{headers});
  }

  createTask(task: Task): Observable<Object>{
    this.username = this.authService.getLoggedInUserName();
    this.password = "kutya";
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
    return this.httpClient.post(this.URLAll, task,{headers});
  }

  updateTask(id: string, task: Task): Observable<Object>{
    this.username = this.authService.getLoggedInUserName();
    this.password = "kutya";
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
    return this.httpClient.put(`${this.URL}/${id}`, task,{headers});
  }

  deleteTask(id: string): Observable<Object>{
    this.username = this.authService.getLoggedInUserName();
    this.password = "kutya";
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(this.username + ':' + this.password) });
    return this.httpClient.delete(`${this.URL}/${id}`,{headers})
  }
}
