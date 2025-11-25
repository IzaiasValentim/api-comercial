package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCode(String code);
    Optional<Produto> findByNameContaining(String name);
}
