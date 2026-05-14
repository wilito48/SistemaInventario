import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ProductoService } from '../../core/services/producto.service';
import { CategoriaService } from '../../core/services/categoria.service';
import { MovimientoService } from '../../core/services/movimiento.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private productoService = inject(ProductoService);
  private categoriaService = inject(CategoriaService);
  private movimientoService = inject(MovimientoService);

  // Uso de Signals para estado reactivo local
  loading = signal(true);
  totalProductos = signal(0);
  totalCategorias = signal(0);
  productosStockBajo = signal(0);
  totalMovimientos = signal(0);

  ngOnInit() {
    this.cargarMetricas();
  }

  cargarMetricas() {
    this.loading.set(true);
    
    forkJoin({
      productos: this.productoService.getAll(),
      categorias: this.categoriaService.getAll(),
      movimientos: this.movimientoService.getAll()
    }).subscribe({
      next: (data) => {
        this.totalProductos.set(data.productos.length);
        this.totalCategorias.set(data.categorias.length);
        this.totalMovimientos.set(data.movimientos.length);
        
        const bajos = data.productos.filter(p => (p.stockActual || 0) <= p.stockMinimo).length;
        this.productosStockBajo.set(bajos);
        
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error cargando métricas', err);
        this.loading.set(false);
      }
    });
  }
}
