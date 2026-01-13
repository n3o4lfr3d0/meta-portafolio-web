import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ADMIN_ROUTE } from '../../../config/admin.config';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-footer',
  standalone: true,
  templateUrl: './footer.component.html'
})
export class FooterComponent {
  year = new Date().getFullYear();
  private readonly router = inject(Router);
  public readonly themeService = inject(ThemeService);

  accessAdmin(event: MouseEvent) {
    // Requires holding Alt key to prevent accidental triggering by visitors
    if (event.altKey) {
      this.router.navigate([ADMIN_ROUTE, 'login']);
    }
  }
}
