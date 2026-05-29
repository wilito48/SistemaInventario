package com.inventario.system.controller;

import com.inventario.system.dto.CategoriaRequestDTO;
import com.inventario.system.dto.CategoriaResponseDTO;
import com.inventario.system.entity.Categoria;
import com.inventario.system.repository.CategoriaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    // ANTES (OCP y IoC): Acceso directo al repositorio, sin capa de Servicio
    private final CategoriaRepository categoriaRepository;

    @GetMapping
    public List<CategoriaResponseDTO> findAll() {
        // ANTES (DRY): Conversión manual repetida en cada método
        return categoriaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaRequestDTO dto) {
        // ANTES (SRP): Construcción manual de la entidad dentro del controlador
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        categoriaRepository.save(categoria);
        return new ResponseEntity<>(convertToDTO(categoria), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoriaRepository.delete(categoria);
            return ResponseEntity.<Void>noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ANTES (DRY): Método de conversión manual duplicado en cada controlador
    private CategoriaResponseDTO convertToDTO(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        dto.setEstado(c.getEstado());
        dto.setFechaCreacion(c.getFechaCreacion());
        return dto;
    }
}
