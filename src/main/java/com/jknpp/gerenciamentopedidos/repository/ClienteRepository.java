package com.jknpp.gerenciamentopedidos.repository;

import com.jknpp.gerenciamentopedidos.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
