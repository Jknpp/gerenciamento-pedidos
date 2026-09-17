package com.jknpp.gerenciamentopedidos.dto;

import com.jknpp.gerenciamentopedidos.domain.StatusPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PedidoResponseDTO {

    private Long id;
    private Long clienteId;
    private String nomeCliente;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private List<ItemPedidoResponseDTO> itens;
}
