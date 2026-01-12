import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Skill } from '../models/skill.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SkillService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/skills`;

  getSkills(lang: string = 'es'): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${this.apiUrl}?lang=${lang}`);
  }
}
