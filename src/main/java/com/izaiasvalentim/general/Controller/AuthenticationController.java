package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.ApiUser.RequestRefreshTokenDTO;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ResponseRefreshTokenDTO;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.UserAuthDTO;
import com.izaiasvalentim.general.Service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("authenticate/login/")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        Authentication authentication = authenticationManager

                .authenticate(new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(), userAuthDTO.getPassword()));

        ResponseRefreshTokenDTO tokenResponse = authenticationService.authenticate(authentication);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("authenticate/refresh-token/")
    public ResponseEntity<?> refresh(@Valid @RequestBody RequestRefreshTokenDTO refreshTokenDTO) {
        ResponseRefreshTokenDTO tokenResponse = authenticationService.refreshToken(refreshTokenDTO.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }
}
