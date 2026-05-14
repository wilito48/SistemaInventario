package com.inventario.system.service.impl;

import com.inventario.system.dto.ProductoRequestDTO;
import com.inventario.system.dto.ProductoResponseDTO;
import com.inventario.system.entity.Categoria;
import com.inventario.system.entity.Producto;
import com.inventario.system.exception.ResourceNotFoundException;
import com.inventario.system.mapper.ProductoMapper;
import com.inventario.system.repository.CategoriaRepository;
import com.inventario.system.repository.ProductoRepository;
import com.inventario.system.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return productoMapper.toDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByCategoriaId(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductoResponseDTO save(ProductoRequestDTO productoDto) {
        if (productoDto.getCodigoBarras() != null && !productoDto.getCodigoBarras().isEmpty()) {
            if (productoRepository.findByCodigoBarras(productoDto.getCodigoBarras()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un producto con el código de barras: " + productoDto.getCodigoBarras());
            }
        }

        Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDto.getCategoriaId()));

        Producto producto = productoMapper.toEntity(productoDto);
        producto.setCategoria(categoria);
        
        Producto savedProducto = productoRepository.save(producto);
        return productoMapper.toDto(savedProducto);
    }

    @Override
    @Transactional
    public ProductoResponseDTO update(Long id, ProductoRequestDTO productoDto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (productoDto.getCodigoBarras() != null && !productoDto.getCodigoBarras().isEmpty()) {
            productoRepository.findByCodigoBarras(productoDto.getCodigoBarras())
                    .filter(p -> !p.getId().equals(id))
                    .ifPresent(p -> {
                        throw new IllegalArgumentException("Ya existe otro producto con el código de barras: " + productoDto.getCodigoBarras());
                    });
        }

        Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDto.getCategoriaId()));

        productoMapper.updateEntityFromDto(productoDto, producto);
        producto.setCategoria(categoria);

        Producto updatedProducto = productoRepository.save(producto);
        return productoMapper.toDto(updatedProducto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }
}
