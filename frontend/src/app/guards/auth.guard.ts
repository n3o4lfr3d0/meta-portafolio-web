import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ADMIN_ROUTE } from '../config/admin.config';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  // Redirect to the correct login page if trying to access the secret route
  return router.createUrlTree([`/${ADMIN_ROUTE}/login`]);
};
