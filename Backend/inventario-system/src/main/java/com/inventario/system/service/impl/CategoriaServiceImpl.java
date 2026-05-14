package com.inventario.system.service.impl;

import com.inventario.system.dto.CategoriaRequestDTO;
import com.inventario.system.dto.CategoriaResponseDTO;
import com.inventario.system.entity.Categoria;
import com.inventario.system.exception.ResourceNotFoundException;
import com.inventario.system.mapper.CategoriaMapper;
import com.inventario.system.repository.CategoriaRepository;
import com.inventario.system.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findAll() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        return categoriaMapper.toDto(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO save(CategoriaRequestDTO categoriaDto) {
        if (categoriaRepository.findByNombre(categoriaDto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaDto.getNombre());
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDto);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(savedCategoria);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO update(Long id, CategoriaRequestDTO categoriaDto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        // Validar si el nombre ya existe y no es la misma categoría
        categoriaRepository.findByNombre(categoriaDto.getNombre())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + categoriaDto.getNombre());
                });

        categoriaMapper.updateEntityFromDto(categoriaDto, categoria);
        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(updatedCategoria);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        categoriaRepository.delete(categoria);
    }
}
