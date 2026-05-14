package com.inventario.system.mapper;

import com.inventario.system.dto.CategoriaRequestDTO;
import com.inventario.system.dto.CategoriaResponseDTO;
import com.inventario.system.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    Categoria toEntity(CategoriaRequestDTO dto);
    CategoriaResponseDTO toDto(Categoria entity);
    void updateEntityFromDto(CategoriaRequestDTO dto, @MappingTarget Categoria entity);
}
