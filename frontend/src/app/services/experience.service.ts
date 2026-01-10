import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Experience } from '../models/experience.model';

@Injectable({
  providedIn: 'root'
})
export class ExperienceService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/experience';

  getExperience(): Observable<Experience[]> {
    return this.http.get<Experience[]>(this.apiUrl);
  }
}
