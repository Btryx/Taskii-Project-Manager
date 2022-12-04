import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Task} from "./task";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private URL = "http://localhost:8082/tasks";
  private URLAll = "http://localhost:8082/tasks/all"

  constructor(private httpClient: HttpClient) { }

  getAllTasks(): Observable<Task[]>{
    return this.httpClient.get<Task[]>(this.URLAll);
  }

  getTask(id: string): Observable<Object>{
    return this.httpClient.get(`${this.URL}/${id}`);
  }

  createTask(task: Task): Observable<Object>{
    return this.httpClient.post(this.URLAll, task);
  }

  updateTask(id: string, task: Task): Observable<Object>{
    return this.httpClient.put(`${this.URL}/${id}`, task);
  }

  deleteTask(id: string): Observable<Object>{
    return this.httpClient.delete(`${this.URL}/${id}`)
  }
}
