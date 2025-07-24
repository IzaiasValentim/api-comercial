package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.ItemAgregado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemAgregadoRepository extends JpaRepository<ItemAgregado, Long> {
    Optional<ItemAgregado> findByItemCode(String code);
}
