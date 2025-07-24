package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.UsuarioBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioBaseRepository extends JpaRepository<UsuarioBase, Long> {
    Optional<UsuarioBase> findByUsername(String username);
    Optional<UsuarioBase> findByEmail(String email);
    Optional<UsuarioBase> findByCPF(String cpf);
}
