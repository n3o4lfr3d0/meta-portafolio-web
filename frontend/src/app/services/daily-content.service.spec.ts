import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../environments/environment';
import { DailyContent, DailyContentService } from './daily-content.service';

describe('DailyContentService', () => {
  let service: DailyContentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DailyContentService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(DailyContentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve daily content from the API', () => {
    const mockContent: DailyContent = {
      date: '2025-01-01',
      tip: 'Coding Tip',
      quote: 'Coding Quote',
      quoteAuthor: 'Author',
      joke: 'Coding Joke'
    };

    service.getDailyContent().subscribe(content => {
      expect(content).toEqual(mockContent);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/daily-content?lang=es`);
    expect(req.request.method).toBe('GET');
    req.flush(mockContent);
  });

  it('should handle API errors', () => {
    const errorMessage = 'Network Error';

    service.getDailyContent().subscribe({
      next: () => {
        // Using expect().toBeFalsy() instead of fail() to avoid the error
        expect(true).toBeFalsy();
      },
      error: (error) => {
        expect(error.status).toBe(500);
        expect(error.statusText).toBe('Internal Server Error');
      }
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/daily-content?lang=es`);
    req.flush(errorMessage, { status: 500, statusText: 'Internal Server Error' });
  });
});
