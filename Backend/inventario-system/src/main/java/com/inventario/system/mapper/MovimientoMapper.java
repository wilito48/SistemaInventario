package com.inventario.system.mapper;

import com.inventario.system.dto.MovimientoRequestDTO;
import com.inventario.system.dto.MovimientoResponseDTO;
import com.inventario.system.entity.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Movimiento toEntity(MovimientoRequestDTO dto);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    @Mapping(source = "usuario.nombre", target = "usuarioNombre")
    MovimientoResponseDTO toDto(Movimiento entity);
}
