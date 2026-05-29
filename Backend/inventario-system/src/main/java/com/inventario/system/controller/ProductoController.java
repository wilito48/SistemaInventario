package com.inventario.system.controller;

import com.inventario.system.dto.CategoriaResponseDTO;
import com.inventario.system.dto.ProductoRequestDTO;
import com.inventario.system.dto.ProductoResponseDTO;
import com.inventario.system.entity.Producto;
import com.inventario.system.repository.ProductoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    // ANTES (OCP y IoC): Acceso directo al repositorio, sin capa de Servicio
    private final ProductoRepository productoRepository;

    @GetMapping
    public List<ProductoResponseDTO> findAll() {
        // ANTES (DRY): Conversión manual repetida en cada método
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> findById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO dto) {
        // ANTES (SRP): Construcción manual de entidad dentro del controlador
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStockMinimo(dto.getStockMinimo());
        productoRepository.save(producto);
        return new ResponseEntity<>(convertToDTO(producto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productoRepository.findById(id).map(producto -> {
            productoRepository.delete(producto);
            return ResponseEntity.<Void>noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ANTES (DRY): Método de conversión manual duplicado en cada controlador
    private ProductoResponseDTO convertToDTO(Producto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setCodigoBarras(p.getCodigoBarras());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecioCompra(p.getPrecioCompra());
        dto.setPrecioVenta(p.getPrecioVenta());
        dto.setStockMinimo(p.getStockMinimo());
        dto.setStockActual(p.getStockActual());
        dto.setEstado(p.getEstado());
        dto.setFechaActualizacion(p.getFechaActualizacion());
        // El objeto Categoria también se mapea manualmente
        if (p.getCategoria() != null) {
            CategoriaResponseDTO catDto = new CategoriaResponseDTO();
            catDto.setId(p.getCategoria().getId());
            catDto.setNombre(p.getCategoria().getNombre());
            dto.setCategoria(catDto);
        }
        return dto;
    }
}
