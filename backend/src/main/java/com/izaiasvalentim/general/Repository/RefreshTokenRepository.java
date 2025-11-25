package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.UsuarioBase;
import com.izaiasvalentim.general.Domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(UsuarioBase user);
}
