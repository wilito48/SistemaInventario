package com.inventario.system.mapper;

import com.inventario.system.dto.UsuarioDTO;
import com.inventario.system.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario entity);

}
