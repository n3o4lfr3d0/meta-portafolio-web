import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  authService = inject(AuthService);
  themeService = inject(ThemeService);
  username = signal('');
  password = signal('');
  showPassword = signal(false);
  error = signal('');
  isLoading = signal(false);

  ngOnInit() {
    this.themeService.toggleTheme('matrix');
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  login() {
    this.isLoading.set(true);
    this.error.set(''); // Clear previous errors
    this.authService.login(this.username(), this.password()).subscribe({
      next: () => {
        this.isLoading.set(false);
      },
      error: () => {
        this.error.set('Credenciales inválidas');
        this.isLoading.set(false);
      }
    });
  }
}
