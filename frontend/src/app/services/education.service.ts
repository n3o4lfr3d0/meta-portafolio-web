import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Education } from '../models/education.model';

@Injectable({
  providedIn: 'root'
})
export class EducationService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/education';

  getEducation(): Observable<Education[]> {
    return this.http.get<Education[]>(this.apiUrl);
  }
}
