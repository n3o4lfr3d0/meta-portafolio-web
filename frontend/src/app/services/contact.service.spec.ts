import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { environment } from '../../environments/environment';
import { ContactRequest, ContactService } from './contact.service';

describe('ContactService', () => {
  let service: ContactService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ContactService,
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router }
      ]
    });
    service = TestBed.inject(ContactService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should submit contact form', () => {
    const request: ContactRequest = {
      name: 'John',
      email: 'john@example.com',
      subject: 'Test',
      message: 'Hello'
    };

    service.submitContact(request).subscribe(response => {
      expect(response).toBe('Success');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/contact`);
    expect(req.request.method).toBe('POST');
    req.flush('Success');
  });
});
