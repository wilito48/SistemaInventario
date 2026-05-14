package com.inventario.system.controller;

import com.inventario.system.dto.MovimientoRequestDTO;
import com.inventario.system.dto.MovimientoResponseDTO;
import com.inventario.system.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> findAll() {
        return ResponseEntity.ok(movimientoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.findById(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<MovimientoResponseDTO>> findByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(movimientoService.findByProductoId(productoId));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoRequestDTO movimientoDto,
            Authentication authentication // Spring Security inyecta el usuario autenticado
    ) {
        // Extraemos el username del token JWT — nunca confiamos en lo que envíe el cliente
        String username = authentication.getName();
        MovimientoResponseDTO created = movimientoService.registrarMovimiento(movimientoDto, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
