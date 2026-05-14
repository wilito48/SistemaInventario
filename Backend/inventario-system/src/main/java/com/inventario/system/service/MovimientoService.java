package com.inventario.system.service;

import com.inventario.system.dto.MovimientoRequestDTO;
import com.inventario.system.dto.MovimientoResponseDTO;

import java.util.List;

public interface MovimientoService {
    List<MovimientoResponseDTO> findAll();
    MovimientoResponseDTO findById(Long id);
    List<MovimientoResponseDTO> findByProductoId(Long productoId);
    MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO movimientoDto, String username);
}
