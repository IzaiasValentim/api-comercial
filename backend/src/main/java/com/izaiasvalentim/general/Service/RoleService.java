package com.izaiasvalentim.general.Service;

import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.Role;
import com.izaiasvalentim.general.Repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cargo de id " + id + " não encontrado."));
    }
}
