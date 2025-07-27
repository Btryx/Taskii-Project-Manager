import { HttpClient, HttpRequest } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  commentUrl : string = 'http://localhost:8082/api/comments';

  http: HttpClient = inject(HttpClient);

  getAllComments(taskId: string) : Observable<any> {
    return this.http.get<any>(`${this.commentUrl}/${taskId}`);
  }

  createComment(comment: Comment) : Observable<any> {
    return this.http.post<Comment>(`${this.commentUrl}/create`, comment);
  }

  deleteComment(id: string) : Observable<any> {
    return this.http.delete<any>(`${this.commentUrl}/delete/${id}`);
  }

}
