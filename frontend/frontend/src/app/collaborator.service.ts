import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class CollaboratorService {

  projectUrl : string = 'http://localhost:8082/api/projects';

  private http: HttpClient = inject(HttpClient);

  getCollaborators(id: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.projectUrl}/${id}/collaborators`);
  }

}
