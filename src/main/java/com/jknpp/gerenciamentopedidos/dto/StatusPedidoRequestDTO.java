package com.jknpp.gerenciamentopedidos.dto;

import com.jknpp.gerenciamentopedidos.domain.StatusPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StatusPedidoRequestDTO {
    private StatusPedido statusPedido;

}
