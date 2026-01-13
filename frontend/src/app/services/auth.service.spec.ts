import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { environment } from '../../environments/environment';
import { ADMIN_ROUTE } from '../config/admin.config';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: any;

  beforeEach(() => {
    routerSpy = { navigate: vi.fn() };
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router, useValue: routerSpy }
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have initial authentication state false when no token', () => {
    expect(service.isAuthenticated()).toBe(false);
  });

  it('should login successfully', () => {
    const mockResponse = { token: 'abc-123' };
    const username = 'admin';
    const password = 'secret';

    service.login(username, password).subscribe(res => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ username, password });
    req.flush(mockResponse);

    expect(localStorage.getItem('admin_token')).toBe('abc-123');
    expect(service.isAuthenticated()).toBe(true);
    expect(routerSpy.navigate).toHaveBeenCalledWith([`/${ADMIN_ROUTE}`]);
  });

  it('should logout successfully', () => {
    localStorage.setItem('admin_token', 'old-token');
    service.isAuthenticated.set(true);

    service.logout();

    expect(localStorage.getItem('admin_token')).toBeNull();
    expect(service.isAuthenticated()).toBe(false);
    expect(routerSpy.navigate).toHaveBeenCalledWith([`/${ADMIN_ROUTE}/login`]);
  });

  it('should initialize as authenticated if token exists', () => {
    localStorage.setItem('admin_token', 'existing-token');

    // Reset testing module to force re-initialization of the service
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router, useValue: routerSpy }
      ]
    });
    const freshService = TestBed.inject(AuthService);

    expect(freshService.isAuthenticated()).toBe(true);
  });
});
