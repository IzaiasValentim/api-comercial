package com.izaiasvalentim.general.Common.Config.Security;

import com.izaiasvalentim.general.Domain.BaseUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class SecUserDetails implements UserDetails {

    private final BaseUser baseUser;

    public SecUserDetails(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return baseUser
                .getRoles()
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return baseUser.getPassword();
    }

    @Override
    public String getUsername() {
        return baseUser.getUsername();
    }
}
