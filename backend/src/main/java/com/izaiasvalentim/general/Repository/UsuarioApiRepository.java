package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.UsuarioApi;
import com.izaiasvalentim.general.Domain.UsuarioBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioApiRepository extends JpaRepository<UsuarioApi, Long> {
    Optional<UsuarioApi> findByUser(UsuarioBase user);
}
