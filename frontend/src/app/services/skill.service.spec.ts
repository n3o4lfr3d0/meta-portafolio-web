import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { SkillService } from './skill.service';
import { Skill } from '../models/skill.model';
import { environment } from '../../environments/environment';

describe('SkillService', () => {
  let service: SkillService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SkillService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(SkillService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve skills from the API', () => {
    const dummySkills: Skill[] = [
      { name: 'Angular', category: 'Frontend', level: 90, icon: 'angular' },
      { name: 'Java', category: 'Backend', level: 95, icon: 'java' }
    ];

    service.getSkills().subscribe(skills => {
      expect(skills.length).toBe(2);
      expect(skills).toEqual(dummySkills);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/skills`);
    expect(req.request.method).toBe('GET');
    req.flush(dummySkills);
  });
});
