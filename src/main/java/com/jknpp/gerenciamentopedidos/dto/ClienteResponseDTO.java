package com.jknpp.gerenciamentopedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private boolean ativo;
    private LocalDateTime dataCadastro;
}
