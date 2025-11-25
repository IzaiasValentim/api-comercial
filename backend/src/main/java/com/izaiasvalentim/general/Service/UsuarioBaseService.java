package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Domain.UsuarioBase;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserRegisterDTO;
import com.izaiasvalentim.general.Domain.Role;
import com.izaiasvalentim.general.Repository.UsuarioBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioBaseService {
    private final UsuarioBaseRepository usuarioBaseRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    @Autowired
    public UsuarioBaseService(UsuarioBaseRepository usuarioBaseRepository, BCryptPasswordEncoder encoder, RoleService roleService) {
        this.usuarioBaseRepository = usuarioBaseRepository;
        this.encoder = encoder;
        this.roleService = roleService;
    }

    public UsuarioBase save(ApiUserRegisterDTO dto) {
        try {
            UsuarioBase usuarioBase = new UsuarioBase();
            usuarioBase.setUsername(dto.username());
            usuarioBase.setEmail(dto.email());
            usuarioBase.setCPF(dto.CPF());
            usuarioBase.setPassword(encoder.encode(dto.password()));

            Role role = roleService.getRoleById((long) dto.role());
            var roles = usuarioBase.getRoles();
            roles.add(role);
            usuarioBase.setRoles(roles);

            return usuarioBaseRepository.save(usuarioBase);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }

    }

    public UsuarioBase findByUsername(String username) {
        return usuarioBaseRepository.findByUsername(username).orElse(null);
    }

    public UsuarioBase findByEmail(String email) {
        return usuarioBaseRepository.findByEmail(email).orElse(null);
    }

    public UsuarioBase findByCPF(String cpf) {
        return usuarioBaseRepository.findByCPF(cpf).orElse(null);
    }
}
