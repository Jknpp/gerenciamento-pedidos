package com.jknpp.gerenciamentopedidos.repository;

import com.jknpp.gerenciamentopedidos.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
