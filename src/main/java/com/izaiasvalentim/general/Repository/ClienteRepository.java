package com.izaiasvalentim.general.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.izaiasvalentim.general.Domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByIdentificationNumber(String identificationNumber);

    Optional<Cliente> findByEmail(String email);

    Page<Cliente> findDistinctByNameContainingAndActive(String name, Boolean active, Pageable pageable);
}
