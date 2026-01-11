import { ApplicationRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { ThemeService } from './theme.service';

describe('ThemeService', () => {
  let service: ThemeService;
  let mockLocalStorage: any;

  beforeEach(() => {
    mockLocalStorage = {};

    // Mock localStorage
    Object.defineProperty(globalThis, 'localStorage', {
      value: {
        getItem: (key: string) => mockLocalStorage[key] || null,
        setItem: (key: string, value: string) => {
          mockLocalStorage[key] = value;
        },
        removeItem: (key: string) => {
          delete mockLocalStorage[key];
        },
        clear: () => {
          mockLocalStorage = {};
        }
      },
      writable: true
    });

    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    service = TestBed.inject(ThemeService);
    expect(service).toBeTruthy();
  });

  it('should have default theme as matrix', () => {
    service = TestBed.inject(ThemeService);
    expect(service.theme()).toBe('matrix');
  });

  it('should toggle theme to light', () => {
    service = TestBed.inject(ThemeService);
    service.toggleTheme('light');
    TestBed.inject(ApplicationRef).tick();
    expect(service.theme()).toBe('light');
    expect(localStorage.getItem('portfolio-theme')).toBe('light');
    expect(document.documentElement.classList.contains('dark')).toBe(false);
    expect(document.body.classList.contains('theme-light')).toBe(true);
  });

  it('should toggle theme back to matrix', () => {
    service = TestBed.inject(ThemeService);
    service.toggleTheme('light');
    TestBed.inject(ApplicationRef).tick();
    service.toggleTheme('matrix');
    TestBed.inject(ApplicationRef).tick();

    expect(service.theme()).toBe('matrix');
    expect(localStorage.getItem('portfolio-theme')).toBe('matrix');
    expect(document.documentElement.classList.contains('dark')).toBe(true);
    expect(document.body.classList.contains('theme-light')).toBe(false);
  });

  it('should initialize with stored theme', () => {
    mockLocalStorage['portfolio-theme'] = 'light';
    service = TestBed.inject(ThemeService);
    expect(service.theme()).toBe('light');
  });
});
