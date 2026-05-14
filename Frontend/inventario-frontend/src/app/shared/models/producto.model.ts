import { Categoria } from './categoria.model';

export interface Producto {
  id?: number;
  codigoBarras: string;
  nombre: string;
  descripcion: string;
  categoria?: Categoria;
  categoriaId?: number; // Para el formulario Request
  precioCompra: number;
  precioVenta: number;
  stockMinimo: number;
  stockActual?: number;
  estado?: string;
  fechaActualizacion?: string;
}
