import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { AuthService } from '../../../services/auth.service';
import { ThemeService } from '../../../services/theme.service';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: any;
  let themeServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      login: vi.fn()
    };

    themeServiceMock = {
      toggleTheme: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LoginComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle theme to dark on init', () => {
    expect(themeServiceMock.toggleTheme).toHaveBeenCalledWith('matrix');
  });

  it('should call login on submit', () => {
    authServiceMock.login.mockReturnValue(of({ token: '123' }));
    component.username.set('admin');
    component.password.set('password');
    component.login();
    expect(authServiceMock.login).toHaveBeenCalledWith('admin', 'password');
  });

  it('should set error on login failure', () => {
    authServiceMock.login.mockReturnValue(throwError(() => new Error('Error')));
    component.username.set('admin');
    component.password.set('wrong');
    component.login();
    expect(component.error()).toBe('Credenciales inválidas');
  });

  it('should toggle password visibility', () => {
    expect(component.showPassword()).toBe(false);
    component.togglePassword();
    expect(component.showPassword()).toBe(true);
    component.togglePassword();
    expect(component.showPassword()).toBe(false);
  });
});
