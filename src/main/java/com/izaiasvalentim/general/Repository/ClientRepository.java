package com.izaiasvalentim.general.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.izaiasvalentim.general.Domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByIdentificationNumber(String identificationNumber);

    Optional<Client> findByEmail(String email);

    Page<Client> findDistinctByNameContainingAndActive(String name, Boolean active, Pageable pageable);
}
