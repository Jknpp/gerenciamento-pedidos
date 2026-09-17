package com.jknpp.gerenciamentopedidos.mapper;

import com.jknpp.gerenciamentopedidos.domain.Cliente;
import com.jknpp.gerenciamentopedidos.dto.ClienteRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.ClienteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    ClienteResponseDTO toDTO(Cliente cliente);
}