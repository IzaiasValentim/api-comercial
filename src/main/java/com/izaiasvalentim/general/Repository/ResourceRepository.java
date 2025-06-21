package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByItemCode(String code);
}
