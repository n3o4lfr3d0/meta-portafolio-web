import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { MatrixRainComponent } from './components/ui/matrix-rain/matrix-rain.component';
import { ThemeSwitcherComponent } from './components/ui/theme-switcher/theme-switcher.component';
import { ToastComponent } from './components/ui/toast/toast.component';
import { ADMIN_ROUTE } from './config/admin.config';
import { ThemeService } from './services/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    MatrixRainComponent,
    ThemeSwitcherComponent,
    ToastComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'frontend';
  themeService = inject(ThemeService);
  private readonly router = inject(Router);

  isAdminRoute = signal(false);

  constructor() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isAdminRoute.set(event.urlAfterRedirects?.includes(`/${ADMIN_ROUTE}`) || event.url?.includes(`/${ADMIN_ROUTE}`));
    });
  }
}
