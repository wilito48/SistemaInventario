import { Component, inject, OnInit, ViewChild, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';

import { CategoriaService } from '../../../core/services/categoria.service';
import { Categoria } from '../../../shared/models/categoria.model';
import { CategoriaFormComponent } from '../categoria-form/categoria-form.component';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-categorias-list',
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
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  templateUrl: './categorias-list.component.html',
  styleUrls: ['./categorias-list.component.scss']
})
export class CategoriasListComponent implements OnInit {
  private categoriaService = inject(CategoriaService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  authService = inject(AuthService);

  // El ALMACENERO no ve la columna de acciones (no puede editar ni eliminar)
  get displayedColumns(): string[] {
    return this.authService.isAdmin()
      ? ['id', 'nombre', 'descripcion', 'acciones']
      : ['id', 'nombre', 'descripcion'];
  }
  dataSource = new MatTableDataSource<Categoria>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriaService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: () => this.mostrarMensaje('Error al cargar categorías', 'error')
    });
  }

  aplicarFiltro(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  abrirFormulario(categoria?: Categoria) {
    const dialogRef = this.dialog.open(CategoriaFormComponent, {
      width: '400px',
      data: categoria || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (categoria?.id) {
          this.categoriaService.update(categoria.id, result).subscribe({
            next: () => {
              this.mostrarMensaje('Categoría actualizada exitosamente');
              this.cargarCategorias();
            },
            error: () => this.mostrarMensaje('Error al actualizar', 'error')
          });
        } else {
          this.categoriaService.create(result).subscribe({
            next: () => {
              this.mostrarMensaje('Categoría creada exitosamente');
              this.cargarCategorias();
            },
            error: () => this.mostrarMensaje('Error al crear', 'error')
          });
        }
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Está seguro de eliminar esta categoría?')) {
      this.categoriaService.delete(id).subscribe({
        next: () => {
          this.mostrarMensaje('Categoría eliminada exitosamente');
          this.cargarCategorias();
        },
        error: () => this.mostrarMensaje('Error al eliminar', 'error')
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
