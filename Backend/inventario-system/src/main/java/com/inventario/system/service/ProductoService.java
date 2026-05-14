package com.inventario.system.service;

import com.inventario.system.dto.ProductoRequestDTO;
import com.inventario.system.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {
    List<ProductoResponseDTO> findAll();
    ProductoResponseDTO findById(Long id);
    List<ProductoResponseDTO> findByCategoriaId(Long categoriaId);
    ProductoResponseDTO save(ProductoRequestDTO productoDto);
    ProductoResponseDTO update(Long id, ProductoRequestDTO productoDto);
    void delete(Long id);
}
