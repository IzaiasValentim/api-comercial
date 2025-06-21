package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Communication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {
    Page<Communication> findByIsDeletedFalseAndEndDateAfter(LocalDate currentDate, Pageable pageable);

    Page<Communication> findByIsDeletedFalse(Pageable pageable);
}
