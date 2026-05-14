import { inject } from '@angular/core';
import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { authGuard } from './core/guards/auth.guard';
import { AuthService } from './core/services/auth.service';

export const routes: Routes = [
  // Ruta pública: Login
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  // Rutas protegidas: requieren autenticación (authGuard)
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'categorias',
        loadComponent: () => import('./pages/categorias/categorias-list/categorias-list.component').then(m => m.CategoriasListComponent)
      },
      {
        path: 'productos',
        loadComponent: () => import('./pages/productos/productos-list/productos-list.component').then(m => m.ProductosListComponent)
      },
      {
        path: 'movimientos',
        loadComponent: () => import('./pages/movimientos/movimientos-list/movimientos-list.component').then(m => m.MovimientosListComponent)
      },
      {
        path: 'usuarios',
        loadComponent: () => import('./pages/usuarios/usuarios-list/usuarios-list.component').then(m => m.UsuariosListComponent),
        canActivate: [() => inject(AuthService).isAdmin()]
      }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
