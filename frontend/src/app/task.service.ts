import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Task} from "./task";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private URL = "http://localhost:8082/api/tasks";
  private URLAll = "http://localhost:8082/api/tasks/all"

  constructor(private httpClient: HttpClient) { }

  getAllTasks(): Observable<Task[]>{
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa("bea" + ':' + "kutya") });
    return this.httpClient.get<Task[]>(this.URLAll,{headers});
  }

  getTask(id: string): Observable<Object>{
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa("bea" + ':' + "kutya") });
    return this.httpClient.get(`${this.URL}/${id}`,{headers});
  }

  createTask(task: Task): Observable<Object>{
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa("bea" + ':' + "kutya") });
    return this.httpClient.post(this.URLAll, task,{headers});
  }

  updateTask(id: string, task: Task): Observable<Object>{
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa("bea" + ':' + "kutya") });
    return this.httpClient.put(`${this.URL}/${id}`, task,{headers});
  }

  deleteTask(id: string): Observable<Object>{
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa("bea" + ':' + "kutya") });
    return this.httpClient.delete(`${this.URL}/${id}`,{headers})
  }
}
