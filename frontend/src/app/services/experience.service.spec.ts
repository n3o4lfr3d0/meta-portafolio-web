import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ExperienceService } from './experience.service';
import { Experience } from '../models/experience.model';

describe('ExperienceService', () => {
  let service: ExperienceService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ExperienceService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ExperienceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve experience list', () => {
    const dummyExperience: Experience[] = [
      { title: 'Dev', company: 'Test Corp', period: '2020', description: 'Desc', link: '#' }
    ];

    service.getExperience().subscribe(experience => {
      expect(experience.length).toBe(1);
      expect(experience).toEqual(dummyExperience);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/experience');
    expect(req.request.method).toBe('GET');
    req.flush(dummyExperience);
  });
});
