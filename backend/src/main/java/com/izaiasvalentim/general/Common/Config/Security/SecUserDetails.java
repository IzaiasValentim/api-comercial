package com.izaiasvalentim.general.Common.Config.Security;

import com.izaiasvalentim.general.Domain.UsuarioBase;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class SecUserDetails implements UserDetails {

    private final UsuarioBase usuarioBase;

    public SecUserDetails(UsuarioBase usuarioBase) {
        this.usuarioBase = usuarioBase;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuarioBase
                .getRoles()
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuarioBase.getPassword();
    }

    @Override
    public String getUsername() {
        return usuarioBase.getUsername();
    }
}
