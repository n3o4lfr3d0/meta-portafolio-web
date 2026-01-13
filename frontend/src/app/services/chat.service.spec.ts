import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../environments/environment';
import { ChatService } from './chat.service';

describe('ChatService', () => {
  let service: ChatService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ChatService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ChatService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send message and return response', () => {
    const dummyResponse = { response: 'Hello from Bot' };
    const userMessage = 'Hi';
    const language = 'en';
    const contextPage = 'home';

    service.sendMessage(userMessage, language, contextPage).subscribe(res => {
      expect(res).toEqual(dummyResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/chat/ask`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ message: userMessage, language, contextPage });

    req.flush(dummyResponse);
  });
});
