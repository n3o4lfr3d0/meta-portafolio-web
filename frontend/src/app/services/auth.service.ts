import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { ADMIN_ROUTE } from '../config/admin.config';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  isAuthenticated = signal<boolean>(!!localStorage.getItem('admin_token'));

  login(username: string, password: string) {
    return this.http.post<{token: string}>(`${environment.apiUrl}/auth/login`, { username, password }).pipe(
      tap(res => {
        localStorage.setItem('admin_token', res.token);
        this.isAuthenticated.set(true);
        this.router.navigate([`/${ADMIN_ROUTE}`]);
      })
    );
  }

  logout() {
    localStorage.removeItem('admin_token');
    this.isAuthenticated.set(false);
    this.router.navigate([`/${ADMIN_ROUTE}/login`]);
  }
}
