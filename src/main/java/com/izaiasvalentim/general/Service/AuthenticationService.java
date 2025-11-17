package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.Config.Jwt.JwtService;
import com.izaiasvalentim.general.Common.Config.Security.SecUserDetails;
import com.izaiasvalentim.general.Domain.UsuarioBase;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ResponseRefreshTokenDTO;
import com.izaiasvalentim.general.Domain.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthenticationService(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }
    
    public ResponseRefreshTokenDTO authenticate(Authentication authentication) {
        String refreshToken = refreshTokenService.createRefreshToken(authentication.getName());
        String token = jwtService.generateToken(authentication);

        return new ResponseRefreshTokenDTO(token, refreshToken);
    }

    public ResponseRefreshTokenDTO refreshToken(String refreshToken) {
        RefreshToken newRefreshToken = refreshTokenService.validateTokenRenewal(refreshToken);

        UsuarioBase user = newRefreshToken.getUser();
        SecUserDetails userDetails = new SecUserDetails(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        String token = jwtService.generateToken(authentication);

        return new ResponseRefreshTokenDTO(token, refreshToken);
    }
}
