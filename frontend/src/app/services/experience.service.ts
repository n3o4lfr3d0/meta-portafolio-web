import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Experience } from '../models/experience.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ExperienceService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/experience`;

  getExperience(): Observable<Experience[]> {
    return this.http.get<Experience[]>(this.apiUrl);
  }
}
