package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
    Optional<BaseUser> findByUsername(String username);
    Optional<BaseUser> findByEmail(String email);
    Optional<BaseUser> findByCPF(String cpf);
}
