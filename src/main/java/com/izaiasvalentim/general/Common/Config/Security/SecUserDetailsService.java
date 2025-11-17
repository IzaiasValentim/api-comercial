package com.izaiasvalentim.general.Common.Config.Security;

import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Repository.UsuarioBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecUserDetailsService implements UserDetailsService {

    private final UsuarioBaseRepository usuarioBaseRepository;

    @Autowired
    public SecUserDetailsService(UsuarioBaseRepository usuarioBaseRepository) {
        this.usuarioBaseRepository = usuarioBaseRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return usuarioBaseRepository.findByUsername(username).map(SecUserDetails::new)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with username: " + username));
    }
}
