package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Comunicado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CommunicadoRepository extends JpaRepository<Comunicado, Long> {
    Page<Comunicado> findByIsDeletedFalseAndEndDateAfter(LocalDate currentDate, Pageable pageable);

    Page<Comunicado> findByIsDeletedFalse(Pageable pageable);
}
