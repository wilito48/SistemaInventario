import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { Producto } from '../../../shared/models/producto.model';
import { Categoria } from '../../../shared/models/categoria.model';
import { CategoriaService } from '../../../core/services/categoria.service';

@Component({
  selector: 'app-producto-form',
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
  templateUrl: './producto-form.component.html',
  styleUrls: ['./producto-form.component.scss']
})
export class ProductoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<ProductoFormComponent>);
  private categoriaService = inject(CategoriaService);
  public data: Producto = inject(MAT_DIALOG_DATA);

  form!: FormGroup;
  categorias: Categoria[] = [];

  ngOnInit() {
    this.cargarCategorias();
    
    this.form = this.fb.group({
      codigoBarras: [this.data?.codigoBarras || '', Validators.required],
      nombre: [this.data?.nombre || '', Validators.required],
      descripcion: [this.data?.descripcion || ''],
      categoriaId: [this.data?.categoria?.id || '', Validators.required],
      precioCompra: [this.data?.precioCompra || 0, [Validators.required, Validators.min(0)]],
      precioVenta: [this.data?.precioVenta || 0, [Validators.required, Validators.min(0)]],
      stockMinimo: [this.data?.stockMinimo || 0, [Validators.required, Validators.min(0)]]
    });
  }

  cargarCategorias() {
    this.categoriaService.getAll().subscribe({
      next: (data) => this.categorias = data,
      error: (err) => console.error('Error cargando categorias', err)
    });
  }

  guardar() {
    if (this.form.valid) {
      // Formatear payload correcto para el backend (DTO)
      const payload = {
        ...this.form.value,
        categoriaId: this.form.value.categoriaId
      };
      this.dialogRef.close(payload);
    }
  }
}
