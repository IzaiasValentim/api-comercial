package com.izaiasvalentim.general.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Common.CustomExceptions.UserAlreadyExistsException;
import com.izaiasvalentim.general.Domain.UsuarioApi;
import com.izaiasvalentim.general.Domain.UsuarioBase;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserRegisterDTO;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserReturnDTO;
import com.izaiasvalentim.general.Repository.UsuarioApiRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioApiService {

    private final UsuarioApiRepository usuarioApiRepository;
    private final UsuarioBaseService usuarioBaseService;

    @Autowired
    public UsuarioApiService(UsuarioApiRepository usuarioApiRepository, UsuarioBaseService usuarioBaseService) {
        this.usuarioApiRepository = usuarioApiRepository;
        this.usuarioBaseService = usuarioBaseService;
    }

    @Transactional
    public ApiUserReturnDTO registerUser(ApiUserRegisterDTO dto) {
        try {
            validateUserUniqueFields(dto);

            UsuarioBase usuarioBase = usuarioBaseService.save(dto);

            UsuarioApi usuarioApi = new UsuarioApi();
            usuarioApi.setUser(usuarioBase);
            usuarioApi.setAddress(dto.address());
            usuarioApi.setPhone(dto.phone());
            usuarioApi.setAdmissionDate(dto.admissionDate());
            usuarioApi.setIsAdmin();
            usuarioApi.setIsActive(true);
            usuarioApi.setDeleted(false);
            usuarioApiRepository.save(usuarioApi);

            return new ApiUserReturnDTO(usuarioApi);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }
    }

    public ApiUserReturnDTO updateUser(ApiUserRegisterDTO dto) {
        try {
            UsuarioApi userToUpdate = usuarioApiRepository.findByUser(usuarioBaseService.findByUsername(dto.username()))
                    .orElseThrow(() ->
                            new ResourceNotFoundException
                                    ("Erro ao atualizar, nenhum usuário encontrato com os dados fornecidos."));

            if (dto.phone() != null && !dto.phone().isEmpty()) {
                userToUpdate.setPhone(dto.phone());
            }
            if (dto.address() != null && !dto.address().isEmpty()) {
                userToUpdate.setAddress(dto.address());
            }
            usuarioApiRepository.save(userToUpdate);
            return new ApiUserReturnDTO(userToUpdate);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Erro interno ao atualizar usuário.");
        }
    }

    public ApiUserReturnDTO getUserByUsername(String username) {
        UsuarioApi user = usuarioApiRepository.findByUser(usuarioBaseService.findByUsername(username)).orElseThrow(() -> new
                ResourceNotFoundException("Nenhum usuário encontrato com os dados fornecidos."));
        return new ApiUserReturnDTO(user);
    }

    public List<ApiUserReturnDTO> getAllUsers() {
        List<ApiUserReturnDTO> returnDTOs = new ArrayList<>();
        for (UsuarioApi user : usuarioApiRepository.findAll()) {
            returnDTOs.add(new ApiUserReturnDTO(user));
        }
        return returnDTOs;
    }

    public void deleteUserByUsername(String username) {
        Optional<UsuarioApi> apiUser = usuarioApiRepository.findByUser(usuarioBaseService.findByUsername(username));
        if (apiUser.isEmpty()) {
            throw new ResourceNotFoundException("Erro ao remover, nenhum usuário encontrato com os dados fornecidos.");
        }
        apiUser.get().setIsActive(false);
        apiUser.get().setDeleted(true);
        usuarioApiRepository.save(apiUser.get());
    }

    private void validateUserUniqueFields(ApiUserRegisterDTO dto) {
        String param = "username";

        UsuarioBase usuarioBase = usuarioBaseService.findByUsername(dto.username());
        param = usuarioBase == null ? "email" : param;

        usuarioBase = usuarioBaseService.findByEmail(dto.email());
        param = usuarioBase == null ? "CPF" : param;

        usuarioBase = usuarioBaseService.findByCPF(dto.CPF());

        if (usuarioBase != null) {
            throw new UserAlreadyExistsException("Um usuário com este mesmo valor do campo: " + param + ", já existe.");
        }
    }
}
