import { Routes } from '@angular/router';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { LoginComponent } from './components/admin/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { authGuard } from './guards/auth.guard';
import { ADMIN_ROUTE } from './config/admin.config';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  // Secure Admin Routes
  { path: `${ADMIN_ROUTE}/login`, component: LoginComponent },
  { path: ADMIN_ROUTE, component: DashboardComponent, canActivate: [authGuard] },
  
  // Fake Admin Route (Honeypot/404)
  { path: 'admin', redirectTo: '' },
  
  { path: '**', redirectTo: '' }
];
