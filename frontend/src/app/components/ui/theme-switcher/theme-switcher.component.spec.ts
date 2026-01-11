import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ThemeSwitcherComponent } from './theme-switcher.component';
import { ThemeService } from '../../../services/theme.service';
import { signal } from '@angular/core';
import { By } from '@angular/platform-browser';
import { vi } from 'vitest';

describe('ThemeSwitcherComponent', () => {
  let component: ThemeSwitcherComponent;
  let fixture: ComponentFixture<ThemeSwitcherComponent>;
  let themeServiceMock: any;

  beforeEach(async () => {
    themeServiceMock = {
      theme: signal('matrix'),
      toggleTheme: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [ThemeSwitcherComponent],
      providers: [
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ThemeSwitcherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle to light theme', () => {
    const lightButton = fixture.debugElement.query(By.css('button[title*="Light Mode"]'));
    lightButton.nativeElement.click();
    expect(themeServiceMock.toggleTheme).toHaveBeenCalledWith('light');
  });

  it('should toggle to matrix theme', () => {
    const matrixButton = fixture.debugElement.query(By.css('button[title*="Matrix Mode"]'));
    matrixButton.nativeElement.click();
    expect(themeServiceMock.toggleTheme).toHaveBeenCalledWith('matrix');
  });
});
