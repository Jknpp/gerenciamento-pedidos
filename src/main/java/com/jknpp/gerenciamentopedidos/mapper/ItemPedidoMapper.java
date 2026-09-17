package com.jknpp.gerenciamentopedidos.mapper;

import com.jknpp.gerenciamentopedidos.domain.ItemPedido;
import com.jknpp.gerenciamentopedidos.dto.ItemPedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.ItemPedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface ItemPedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "precoUnitario", ignore = true)
    ItemPedido toEntity(ItemPedidoRequestDTO dto);

    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    ItemPedidoResponseDTO toDTO(ItemPedido itemPedido);
}