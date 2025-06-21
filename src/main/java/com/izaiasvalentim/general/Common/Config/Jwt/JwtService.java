package com.izaiasvalentim.general.Common.Config.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.izaiasvalentim.general.Common.utils.TimeUtils.getCurrentTimeTruncatedInSeconds;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    @Value("${tokens.expiration.access.exp}")
    private Integer accessTokenExpiresInSeconds;

    @Autowired
    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = getCurrentTimeTruncatedInSeconds();
        Instant expiration = now.plusSeconds(accessTokenExpiresInSeconds);

        String scopes = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("General")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
