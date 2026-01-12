import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/comments`;

  getApprovedComments(): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.apiUrl);
  }

  addComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.apiUrl, comment);
  }

  getAllComments(): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/admin`);
  }

  approveComment(id: string): Observable<Comment> {
    return this.http.put<Comment>(`${this.apiUrl}/${id}/approve`, {});
  }

  updateComment(id: string, content: string, token: string): Observable<Comment> {
    return this.http.put<Comment>(`${this.apiUrl}/${id}?token=${token}`, { content });
  }

  deleteComment(id: string, token?: string): Observable<void> {
    const url = token ? `${this.apiUrl}/${id}?token=${token}` : `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }
}
