package com.jknpp.gerenciamentopedidos.repository;

import com.jknpp.gerenciamentopedidos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
