import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../environments/environment';
import { Language } from '../models/language.model';
import { LanguageService } from './language.service';

describe('LanguageService', () => {
  let service: LanguageService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        LanguageService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(LanguageService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all languages', () => {
    const dummyLanguages: Language[] = [
      { name: 'Spanish', level: 'Native', code: 'es', percentage: 100 },
      { name: 'English', level: 'Intermediate', code: 'en', percentage: 60 }
    ];

    service.getAllLanguages().subscribe(languages => {
      expect(languages.length).toBe(2);
      expect(languages).toEqual(dummyLanguages);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/languages?lang=es`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyLanguages);
  });
});
