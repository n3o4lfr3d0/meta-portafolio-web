import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  authService = inject(AuthService);
  themeService = inject(ThemeService);
  password = signal('');
  showPassword = signal(false);
  error = signal('');

  ngOnInit() {
    this.themeService.toggleTheme('light');
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  login() {
    this.authService.login(this.password()).subscribe({
      error: () => this.error.set('Invalid credentials')
    });
  }
}
