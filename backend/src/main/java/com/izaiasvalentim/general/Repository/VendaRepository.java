package com.izaiasvalentim.general.Repository;


import com.izaiasvalentim.general.Domain.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID> {
    Optional<Venda> findByIdAndIsDeleted(UUID id,  boolean deleted);

    // Busca a venda carregando os itens e o cliente de uma vez (Performance)
    @Query("SELECT v FROM Venda v JOIN FETCH v.itemCompras ic JOIN FETCH ic.item JOIN FETCH v.cliente WHERE v.id = :id")
    Optional<Venda> findByIdWithItems(UUID id);

    @Query("SELECT v FROM Venda v JOIN v.cliente c WHERE " +
            "(:cpf IS NULL OR c.identificationNumber LIKE %:cpf%) AND " +
            "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:status IS NULL OR v.statusCompraId = :status)")
    Page<Venda> findByFilters(
            @Param("cpf") String cpf,
            @Param("name") String name,
            @Param("status") Integer status,
            Pageable pageable);
}
