import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { AuthResponse, CurrentUser, LoginRequest } from '../../shared/models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly API_URL = 'http://localhost:8080/api/v1/auth';
  private readonly TOKEN_KEY = 'inv_token';
  private readonly USER_KEY = 'inv_user';

  private http = inject(HttpClient);
  private router = inject(Router);

  // Signal reactivo con el usuario actual (null = no autenticado)
  currentUser = signal<CurrentUser | null>(this.getUserFromStorage());

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => {
        // Guardamos token y datos del usuario en localStorage
        localStorage.setItem(this.TOKEN_KEY, response.token);
        const user: CurrentUser = {
          username: response.username,
          nombre: response.nombre,
          rol: response.rol
        };
        localStorage.setItem(this.USER_KEY, JSON.stringify(user));
        this.currentUser.set(user);
      })
    );
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.currentUser()?.rol === 'ADMIN';
  }

  private getUserFromStorage(): CurrentUser | null {
    const stored = localStorage.getItem(this.USER_KEY);
    return stored ? JSON.parse(stored) : null;
  }
}
