import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Education } from '../models/education.model';

@Injectable({
  providedIn: 'root'
})
export class EducationService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/education`;

  getEducation(lang: string = 'es'): Observable<Education[]> {
    return this.http.get<Education[]>(`${this.apiUrl}?lang=${lang}`);
  }
}
