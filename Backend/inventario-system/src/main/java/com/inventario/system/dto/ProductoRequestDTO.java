package com.inventario.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequestDTO {
    private String codigoBarras;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    @NotNull(message = "El precio de compra es obligatorio")
    @Positive(message = "El precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;
    
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;
}
