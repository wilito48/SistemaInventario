package com.inventario.system.controller;

import com.inventario.system.dto.UsuarioDTO;
import com.inventario.system.dto.UsuarioSaveDTO;
import com.inventario.system.entity.Usuario;
import com.inventario.system.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    // ANTES (OCP y IoC): Acceso directo al repositorio, sin capa de Servicio
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UsuarioDTO> getAll() {
        // ANTES (DRY): Conversión manual repetida en cada método
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UsuarioSaveDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
        }
        
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria para nuevos usuarios");
        }

        // ANTES (SRP): Construcción manual de la entidad dentro del controlador
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .username(dto.getUsername())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol())
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        usuarioRepository.save(usuario);
        return ResponseEntity.ok(convertToDTO(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UsuarioSaveDTO dto) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(dto.getNombre());
            usuario.setRol(dto.getRol());
            usuario.setEstado(dto.getEstado());
            
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            }
            
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(convertToDTO(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuarioRepository.delete(usuario);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ANTES (DRY): Método de conversión manual duplicado en cada controlador
    private UsuarioDTO convertToDTO(Usuario u) {
        return UsuarioDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .username(u.getUsername())
                .rol(u.getRol())
                .estado(u.getEstado())
                .fechaCreacion(u.getFechaCreacion())
                .build();
    }
}
