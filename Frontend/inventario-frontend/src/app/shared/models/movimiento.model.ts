export interface Movimiento {
  id?: number;
  productoId: number;
  productoNombre?: string;
  tipo: 'ENTRADA' | 'SALIDA';
  cantidad: number;
  motivo: string;
  fechaMovimiento?: string;
  usuarioNombre?: string;
}
