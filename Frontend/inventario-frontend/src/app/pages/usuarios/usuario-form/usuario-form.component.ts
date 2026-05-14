import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatIconModule } from '@angular/material/icon';
import { Usuario } from '../../../core/services/usuario.service';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatIconModule
  ],
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.scss']
})
export class UsuarioFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<UsuarioFormComponent>);
  data = inject<Usuario | null>(MAT_DIALOG_DATA);

  form!: FormGroup;
  isEdit = false;
  hidePassword = true;

  ngOnInit() {
    this.isEdit = !!this.data;
    
    this.form = this.fb.group({
      nombre: [this.data?.nombre || '', [Validators.required, Validators.maxLength(100)]],
      username: [this.data?.username || '', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
      password: ['', this.isEdit ? [] : [Validators.required, Validators.minLength(6)]],
      rol: [this.data?.rol || 'ALMACENERO', Validators.required],
      estado: [this.data?.estado ?? true]
    });

    if (this.isEdit) {
      this.form.get('username')?.disable(); // No permitir cambiar el username al editar
    }
  }

  guardar() {
    if (this.form.valid) {
      // Si estamos editando y el password está vacío, lo quitamos del objeto para no enviarlo
      const value = { ...this.form.getRawValue() };
      if (this.isEdit && (!value.password || value.password.trim() === '')) {
        delete value.password;
      }
      this.dialogRef.close(value);
    }
  }
}
