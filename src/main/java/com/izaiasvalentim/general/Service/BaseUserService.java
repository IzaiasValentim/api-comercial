package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Domain.BaseUser;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserRegisterDTO;
import com.izaiasvalentim.general.Domain.Role;
import com.izaiasvalentim.general.Repository.BaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService {
    private final BaseUserRepository baseUserRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    @Autowired
    public BaseUserService(BaseUserRepository baseUserRepository, BCryptPasswordEncoder encoder, RoleService roleService) {
        this.baseUserRepository = baseUserRepository;
        this.encoder = encoder;
        this.roleService = roleService;
    }

    public BaseUser save(ApiUserRegisterDTO dto) {
        try {
            BaseUser baseUser = new BaseUser();
            baseUser.setUsername(dto.username());
            baseUser.setEmail(dto.email());
            baseUser.setCPF(dto.CPF());
            baseUser.setPassword(encoder.encode(dto.password()));

            Role role = roleService.getRoleById((long) dto.role());
            var roles = baseUser.getRoles();
            roles.add(role);
            baseUser.setRoles(roles);

            return baseUserRepository.save(baseUser);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }

    }

    public BaseUser findByUsername(String username) {
        return baseUserRepository.findByUsername(username).orElse(null);
    }

    public BaseUser findByEmail(String email) {
        return baseUserRepository.findByEmail(email).orElse(null);
    }

    public BaseUser findByCPF(String cpf) {
        return baseUserRepository.findByCPF(cpf).orElse(null);
    }
}
