import { Component, inject, OnInit, ViewChild } from '@angular/core';
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

import { MovimientoService } from '../../../core/services/movimiento.service';
import { Movimiento } from '../../../shared/models/movimiento.model';
import { MovimientoFormComponent } from '../movimiento-form/movimiento-form.component';

@Component({
  selector: 'app-movimientos-list',
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
    MatSnackBarModule
  ],
  templateUrl: './movimientos-list.component.html',
  styleUrls: ['./movimientos-list.component.scss']
})
export class MovimientosListComponent implements OnInit {
  private movimientoService = inject(MovimientoService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['fecha', 'tipo', 'producto', 'cantidad', 'motivo'];
  dataSource = new MatTableDataSource<Movimiento>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.cargarMovimientos();
  }

  cargarMovimientos() {
    this.movimientoService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: () => this.mostrarMensaje('Error al cargar historial', 'error')
    });
  }

  aplicarFiltro(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    // Búsqueda por productoNombre o motivo
    this.dataSource.filterPredicate = (data: Movimiento, filter: string) => {
      const dataStr = (data.productoNombre || '') + data.motivo + data.tipo;
      return dataStr.toLowerCase().indexOf(filter) != -1;
    };
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  abrirFormulario() {
    const dialogRef = this.dialog.open(MovimientoFormComponent, {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.movimientoService.create(result).subscribe({
          next: () => {
            this.mostrarMensaje('Movimiento registrado exitosamente');
            this.cargarMovimientos();
          },
          error: (err) => {
            // El backend lanza BAD_REQUEST si no hay stock suficiente para una salida
            const msgError = err.error?.message || 'Error al registrar movimiento';
            this.mostrarMensaje(msgError, 'error');
          }
        });
      }
    });
  }

  private mostrarMensaje(mensaje: string, tipo: 'success' | 'error' = 'success') {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 4000,
      panelClass: tipo === 'success' ? ['chip-ok'] : ['chip-baja'],
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });
  }
}
