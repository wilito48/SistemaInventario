import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { Producto } from '../../../shared/models/producto.model';
import { ProductoService } from '../../../core/services/producto.service';

@Component({
  selector: 'app-movimiento-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatSelectModule
  ],
  templateUrl: './movimiento-form.component.html',
  styleUrls: ['./movimiento-form.component.scss']
})
export class MovimientoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<MovimientoFormComponent>);
  private productoService = inject(ProductoService);

  form!: FormGroup;
  productos: Producto[] = [];

  ngOnInit() {
    this.cargarProductos();
    
    this.form = this.fb.group({
      tipo: ['ENTRADA', Validators.required],
      productoId: ['', Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      motivo: ['', Validators.required]
    });
  }

  cargarProductos() {
    this.productoService.getAll().subscribe({
      next: (data) => this.productos = data,
      error: (err) => console.error('Error cargando productos', err)
    });
  }

  guardar() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
}
