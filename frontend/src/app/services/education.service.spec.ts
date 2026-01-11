import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../environments/environment';
import { Education } from '../models/education.model';
import { EducationService } from './education.service';

describe('EducationService', () => {
  let service: EducationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EducationService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(EducationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve education list', () => {
    const dummyEducation: Education[] = [
      {
        institution: 'Test Uni', degree: 'Bachelor', period: '2020 - 2024',
        description: '',
        link: ''
      }
    ];

    service.getEducation().subscribe(education => {
      expect(education.length).toBe(1);
      expect(education).toEqual(dummyEducation);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/education`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyEducation);
  });
});
