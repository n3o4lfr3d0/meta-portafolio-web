import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ApiCardsComponent } from './api-cards.component';
import { DailyContentService } from '../../services/daily-content.service';
import { ThemeService } from '../../services/theme.service';
import { of, throwError } from 'rxjs';
import { signal } from '@angular/core';
import { vi, describe, it, expect, beforeEach } from 'vitest';

describe('ApiCardsComponent', () => {
  let component: ApiCardsComponent;
  let fixture: ComponentFixture<ApiCardsComponent>;
  let dailyContentServiceMock: any;
  let themeServiceMock: any;

  const mockContent = {
    date: '2025-01-01',
    tip: 'Test Tip',
    quote: 'Test Quote',
    quoteAuthor: 'Author',
    joke: 'Test Joke'
  };

  beforeEach(async () => {
    dailyContentServiceMock = {
      getDailyContent: vi.fn().mockReturnValue(of(mockContent))
    };

    themeServiceMock = {
      language: signal('es')
    };

    await TestBed.configureTestingModule({
      imports: [ApiCardsComponent],
      providers: [
        { provide: DailyContentService, useValue: dailyContentServiceMock },
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ApiCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load content on init based on language', () => {
    expect(dailyContentServiceMock.getDailyContent).toHaveBeenCalledWith('es');
    expect(component.content()).toEqual(mockContent);
    expect(component.isLoading()).toBe(false);
  });

  it('should update content when language changes', () => {
    dailyContentServiceMock.getDailyContent.mockClear();
    themeServiceMock.language.set('en');
    fixture.detectChanges();
    
    expect(dailyContentServiceMock.getDailyContent).toHaveBeenCalledWith('en');
  });

  it('should handle error when loading content', () => {
    dailyContentServiceMock.getDailyContent.mockReturnValue(throwError(() => new Error('API Error')));
    // Trigger update to force error
    themeServiceMock.language.set('en'); 
    fixture.detectChanges();

    expect(component.error()).toBe('Could not load daily content.');
    expect(component.isLoading()).toBe(false);
  });
});
