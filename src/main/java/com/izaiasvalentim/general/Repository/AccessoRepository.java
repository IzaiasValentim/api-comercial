package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoRepository extends JpaRepository<Acesso, Long> {
}
