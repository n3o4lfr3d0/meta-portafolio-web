import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ADMIN_ROUTE } from '../../../config/admin.config';

@Component({
  selector: 'app-footer',
  standalone: true,
  template: `
    <footer class="footer footer-center p-10 bg-base-200 text-base-content rounded">
      <nav>
        <div class="grid grid-flow-col gap-4">
          <a href="https://linkedin.com/in/alfredosotonolazco" target="_blank" class="link link-hover">LinkedIn</a>
          <a href="https://github.com/n3o4lfr3d0" target="_blank" class="link link-hover">GitHub</a>
        </div>
      </nav>
      <aside>
        <p>Copyright © {{ year }} - All right reserved by <span (dblclick)="accessAdmin($event)" class="cursor-default select-none hover:text-primary transition-colors font-bold" title="Alfredo Soto">Alfredo Soto</span></p>
      </aside>
    </footer>
  `
})
export class FooterComponent {
  year = new Date().getFullYear();
  private readonly router = inject(Router);

  accessAdmin(event: MouseEvent) {
    // Requires holding Alt key to prevent accidental triggering by visitors
    if (event.altKey) {
      this.router.navigate([ADMIN_ROUTE, 'login']);
    }
  }
}
