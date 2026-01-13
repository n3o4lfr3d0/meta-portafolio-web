import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ContactRequest {
  name: string;
  email: string;
  subject: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/contact`;

  submitContact(request: ContactRequest): Observable<any> {
    return this.http.post(this.apiUrl, request, { responseType: 'text' });
  }
}
