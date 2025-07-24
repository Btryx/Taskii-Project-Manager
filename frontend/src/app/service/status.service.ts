import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Status } from '../model/status';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  statusUrl : string = 'http://localhost:8082/api/statuses';

  private http: HttpClient = inject(HttpClient);

  getStatuses(projectId: string) : Observable<any> {
    return this.http.get<any>(`${this.statusUrl}/${projectId}/all`);
  }

  createStatus(status: Status) : Observable<any> {
    return this.http.post<any>(`${this.statusUrl}/create`, status);
  }

  updateStatus(status: Status) : Observable<any> {
    return this.http.put<any>(`${this.statusUrl}/update/${status.statusId}`, status);
  }

  deleteStatus(id: string) : Observable<any> {
    return this.http.delete<any>(`${this.statusUrl}/delete/${id}`);
  }

}
