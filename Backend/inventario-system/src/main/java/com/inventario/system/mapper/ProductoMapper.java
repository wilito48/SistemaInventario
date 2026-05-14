package com.inventario.system.mapper;

import com.inventario.system.dto.ProductoRequestDTO;
import com.inventario.system.dto.ProductoResponseDTO;
import com.inventario.system.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface ProductoMapper {
    
    @Mapping(target = "categoria", ignore = true) // Lo asignaremos manualmente en el servicio
    Producto toEntity(ProductoRequestDTO dto);
    
    ProductoResponseDTO toDto(Producto entity);
    
    @Mapping(target = "categoria", ignore = true)
    void updateEntityFromDto(ProductoRequestDTO dto, @MappingTarget Producto entity);
}
