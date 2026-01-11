import { PLATFORM_ID } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { DailyContentService } from '../../services/daily-content.service';
import { ApiCardsComponent } from './api-cards.component';

describe('ApiCardsComponent', () => {
  let component: ApiCardsComponent;
  let fixture: ComponentFixture<ApiCardsComponent>;
  let dailyContentServiceMock: any;

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

    await TestBed.configureTestingModule({
      imports: [ApiCardsComponent],
      providers: [
        { provide: DailyContentService, useValue: dailyContentServiceMock },
        { provide: PLATFORM_ID, useValue: 'browser' }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ApiCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load content on init', () => {
    expect(dailyContentServiceMock.getDailyContent).toHaveBeenCalled();
    expect(component.content()).toEqual(mockContent);
    expect(component.isLoading()).toBe(false);
  });

  it('should handle error when loading content', () => {
    dailyContentServiceMock.getDailyContent.mockReturnValue(throwError(() => new Error('API Error')));
    component.loadContent();

    expect(component.error()).toBe('No se pudo cargar el contenido diario.');
    expect(component.isLoading()).toBe(false);
  });
});
