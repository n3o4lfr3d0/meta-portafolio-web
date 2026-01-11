import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface DailyContent {
  date: string;
  tip: string;
  quote: string;
  quoteAuthor: string;
  joke: string;
}

@Injectable({
  providedIn: 'root'
})
export class DailyContentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/daily-content`;

  getDailyContent(): Observable<DailyContent> {
    return this.http.get<DailyContent>(this.apiUrl);
  }
}
