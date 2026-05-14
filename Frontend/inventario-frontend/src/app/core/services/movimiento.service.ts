import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Movimiento } from '../../shared/models/movimiento.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovimientoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.API_URL}/movimientos`;

  getAll(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(this.apiUrl);
  }

  getByProducto(productoId: number): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(`${this.apiUrl}/producto/${productoId}`);
  }

  getById(id: number): Observable<Movimiento> {
    return this.http.get<Movimiento>(`${this.apiUrl}/${id}`);
  }

  create(movimiento: Partial<Movimiento>): Observable<Movimiento> {
    return this.http.post<Movimiento>(this.apiUrl, movimiento);
  }
}
