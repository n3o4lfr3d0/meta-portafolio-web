import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ADMIN_ROUTE } from './config/admin.config';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  // Secure Admin Routes
  {
    path: `${ADMIN_ROUTE}/login`,
    loadComponent: () => import('./components/admin/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: ADMIN_ROUTE,
    loadComponent: () => import('./components/admin/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard]
  },

  // Fake Admin Route (Honeypot/404)
  { path: 'admin', redirectTo: '' },

  { path: '**', redirectTo: '' }
];
