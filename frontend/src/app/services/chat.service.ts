import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ChatRequest {
  message: string;
  language: string;
  contextPage?: string;
}

export interface ChatResponse {
  response: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly apiUrl = `${environment.apiUrl}/chat`; // Ajustar según environment

  constructor(private readonly http: HttpClient) {}

  sendMessage(message: string, language: string, contextPage?: string): Observable<ChatResponse> {
    return this.http.post<ChatResponse>(`${this.apiUrl}/ask`, { message, language, contextPage });
  }
}
