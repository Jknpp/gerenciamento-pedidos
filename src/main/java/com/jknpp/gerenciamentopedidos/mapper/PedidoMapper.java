package com.jknpp.gerenciamentopedidos.mapper;

import com.jknpp.gerenciamentopedidos.domain.Pedido;
import com.jknpp.gerenciamentopedidos.dto.PedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface PedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "dataPedido", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    Pedido toEntity(PedidoRequestDTO dto);

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    PedidoResponseDTO toDTO(Pedido pedido);
}