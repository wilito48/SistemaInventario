package com.inventario.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MovimientoRequestDTO {
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    // usuarioId ya NO viene del frontend: se extrae del JWT automáticamente en el controlador
    // Esto mejora la seguridad e integridad de los datos de auditoría

    @NotBlank(message = "El tipo de movimiento es obligatorio (ENTRADA o SALIDA)")
    private String tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    private String motivo;
}
