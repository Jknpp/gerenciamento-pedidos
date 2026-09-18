package com.jknpp.gerenciamentopedidos.repository;

import com.jknpp.gerenciamentopedidos.domain.Cliente;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
