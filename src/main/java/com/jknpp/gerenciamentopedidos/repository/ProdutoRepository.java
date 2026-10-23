package com.jknpp.gerenciamentopedidos.repository;

import com.jknpp.gerenciamentopedidos.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNome(String nome);
    List<Produto> findByAtivoTrue();
    Optional<Produto> findByIdAndAtivoTrue(Long id);
}
