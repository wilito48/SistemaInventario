package com.inventario.system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductoResponseDTO {
    private Long id;
    private String codigoBarras;
    private String nombre;
    private String descripcion;
    private CategoriaResponseDTO categoria;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockMinimo;
    private Integer stockActual;
    private Boolean estado;
    private LocalDateTime fechaActualizacion;
}
