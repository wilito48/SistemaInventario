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
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';

import { UsuarioService, Usuario } from '../../../core/services/usuario.service';
import { UsuarioFormComponent } from '../usuario-form/usuario-form.component';

@Component({
  selector: 'app-usuarios-list',
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
    MatTooltipModule,
    MatChipsModule
  ],
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.scss']
})
export class UsuariosListComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['username', 'nombre', 'rol', 'estado', 'fechaCreacion', 'acciones'];
  dataSource = new MatTableDataSource<Usuario>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: () => this.mostrarMensaje('Error al cargar usuarios', 'error')
    });
  }

  aplicarFiltro(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  abrirFormulario(usuario?: Usuario) {
    const dialogRef = this.dialog.open(UsuarioFormComponent, {
      width: '500px',
      data: usuario || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (usuario?.id) {
          this.usuarioService.update(usuario.id, result).subscribe({
            next: () => {
              this.mostrarMensaje('Usuario actualizado exitosamente');
              this.cargarUsuarios();
            },
            error: (err) => this.mostrarMensaje(err.error || 'Error al actualizar', 'error')
          });
        } else {
          this.usuarioService.create(result).subscribe({
            next: () => {
              this.mostrarMensaje('Usuario creado exitosamente');
              this.cargarUsuarios();
            },
            error: (err) => this.mostrarMensaje(err.error || 'Error al crear', 'error')
          });
        }
      }
    });
  }

  eliminar(usuario: Usuario) {
    if (confirm(`¿Está seguro de eliminar al usuario "${usuario.username}"?`)) {
      this.usuarioService.delete(usuario.id!).subscribe({
        next: () => {
          this.mostrarMensaje('Usuario eliminado exitosamente');
          this.cargarUsuarios();
        },
        error: () => this.mostrarMensaje('Error al eliminar usuario', 'error')
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
