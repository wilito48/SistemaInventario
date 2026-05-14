export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  nombre: string;
  rol: string;
  expiresIn: number;
}

export interface CurrentUser {
  username: string;
  nombre: string;
  rol: string;
}
