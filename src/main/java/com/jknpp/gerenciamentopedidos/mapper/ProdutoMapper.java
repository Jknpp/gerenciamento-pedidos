package com.jknpp.gerenciamentopedidos.mapper;

import com.jknpp.gerenciamentopedidos.domain.Produto;
import com.jknpp.gerenciamentopedidos.dto.ProdutoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.ProdutoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toDTO(Produto produto);
}