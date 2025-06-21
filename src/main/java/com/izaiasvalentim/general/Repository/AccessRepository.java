package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
}
