package com.inventario.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSaveDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;
    
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 50)
    private String username;
    
    // El password solo es obligatorio al crear. Al editar puede ser nulo para no cambiarlo.
    private String password;
    
    @NotBlank(message = "El rol es obligatorio")
    private String rol;
    
    private Boolean estado;
}
