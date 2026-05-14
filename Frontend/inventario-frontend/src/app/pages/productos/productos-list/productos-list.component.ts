import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ProductoService } from '../../../core/services/producto.service';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Producto } from '../../../shared/models/producto.model';
import { Categoria } from '../../../shared/models/categoria.model';
import { ProductoFormComponent } from '../producto-form/producto-form.component';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-productos-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  templateUrl: './productos-list.component.html',
  styleUrls: ['./productos-list.component.scss']
})
export class ProductosListComponent implements OnInit {
  private productoService = inject(ProductoService);
  private categoriaService = inject(CategoriaService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  authService = inject(AuthService);

  get displayedColumns(): string[] {
    return this.authService.isAdmin()
      ? ['codigoBarras', 'nombre', 'precio', 'stock', 'acciones']
      : ['codigoBarras', 'nombre', 'precio', 'stock'];
  }
  dataSource = new MatTableDataSource<Producto>();
  categorias: Categoria[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.cargarCategorias();
    this.cargarProductos();
  }

  cargarCategorias() {
    this.categoriaService.getAll().subscribe(data => this.categorias = data);
  }

  cargarProductos() {
    this.productoService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: () => this.mostrarMensaje('Error al cargar productos', 'error')
    });
  }

  filtrarPorCategoria(categoriaId: number) {
    if (categoriaId === 0) {
      this.cargarProductos();
    } else {
      this.productoService.getByCategoria(categoriaId).subscribe({
        next: (data) => {
          this.dataSource.data = data;
        },
        error: () => this.mostrarMensaje('Error al filtrar productos', 'error')
      });
    }
  }

  aplicarFiltro(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    // Búsqueda personalizada: buscar por nombre o código de barras
    this.dataSource.filterPredicate = (data: Producto, filter: string) => {
      const dataStr = data.nombre + data.codigoBarras + (data.categoria?.nombre || '');
      return dataStr.toLowerCase().indexOf(filter) != -1;
    };
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  isStockBajo(producto: Producto): boolean {
    const actual = producto.stockActual || 0;
    return actual <= producto.stockMinimo;
  }

  abrirFormulario(producto?: Producto) {
    const dialogRef = this.dialog.open(ProductoFormComponent, {
      width: '500px',
      data: producto || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (producto?.id) {
          this.productoService.update(producto.id, result).subscribe({
            next: () => {
              this.mostrarMensaje('Producto actualizado exitosamente');
              this.cargarProductos();
            },
            error: () => this.mostrarMensaje('Error al actualizar', 'error')
          });
        } else {
          this.productoService.create(result).subscribe({
            next: () => {
              this.mostrarMensaje('Producto creado exitosamente');
              this.cargarProductos();
            },
            error: () => this.mostrarMensaje('Error al crear', 'error')
          });
        }
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Está seguro de eliminar este producto? Tenga cuidado con productos que ya tienen movimientos.')) {
      this.productoService.delete(id).subscribe({
        next: () => {
          this.mostrarMensaje('Producto eliminado exitosamente');
          this.cargarProductos();
        },
        error: () => this.mostrarMensaje('Error al eliminar. Verifique que no tenga dependencias', 'error')
      });
    }
  }

  private mostrarMensaje(mensaje: string, tipo: 'success' | 'error' = 'success') {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: tipo === 'success' ? ['chip-ok'] : ['chip-baja'],
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });
  }
}
