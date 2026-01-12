import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Language } from '../models/language.model';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/languages`;

  getAllLanguages(lang: string = 'es'): Observable<Language[]> {
    return this.http.get<Language[]>(`${this.apiUrl}?lang=${lang}`);
  }
}
