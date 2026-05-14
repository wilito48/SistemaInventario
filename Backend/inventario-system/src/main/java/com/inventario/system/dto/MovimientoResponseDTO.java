package com.inventario.system.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MovimientoResponseDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String tipo;
    private Integer cantidad;
    private String motivo;
    private LocalDateTime fechaMovimiento;
    private String usuarioNombre;
}
