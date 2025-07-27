import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { Member } from '../models/member';

@Injectable({
  providedIn: 'root'
})
export class CollaboratorService {

  projectUrl : string = 'http://localhost:8082/api/projects';

  private http: HttpClient = inject(HttpClient);

  getCollaboratorsAsUsers(id: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.projectUrl}/${id}/users`);
  }

  getCollaborators(id: string): Observable<Member[]> {
    return this.http.get<Member[]>(`${this.projectUrl}/${id}/collaborators`);
  }

  createCollaborator(member : Member): Observable<any> {
    return this.http.post<any>(`${this.projectUrl}/${member.projectId}/collaborator`, member);
  }

  deleteCollaborator(id : string): Observable<any> {
    return this.http.delete<any>(`${this.projectUrl}/collaborator/${id}`);
  }

}
