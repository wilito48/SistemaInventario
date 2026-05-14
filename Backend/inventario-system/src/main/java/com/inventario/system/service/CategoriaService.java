package com.inventario.system.service;

import com.inventario.system.dto.CategoriaRequestDTO;
import com.inventario.system.dto.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponseDTO> findAll();
    CategoriaResponseDTO findById(Long id);
    CategoriaResponseDTO save(CategoriaRequestDTO categoriaDto);
    CategoriaResponseDTO update(Long id, CategoriaRequestDTO categoriaDto);
    void delete(Long id);
}
