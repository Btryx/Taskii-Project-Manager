import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class CollaboratorService {

  projectUrl : string = 'http://localhost:8082/api/projects';

  private http: HttpClient = inject(HttpClient);

  getCollaborators(id: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.projectUrl}/${id}/collaborators`);
  }

  createCollaborator(projectId: string, userId: string, role: string): Observable<any> {
    return this.http.post<any>(`${this.projectUrl}/${projectId}/collaborator`, {"projectId": projectId, "userId": userId, "role": role});
  }

}
