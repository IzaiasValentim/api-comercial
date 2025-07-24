package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Companhia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanhiaRepository extends JpaRepository<Companhia, Integer> {
}
