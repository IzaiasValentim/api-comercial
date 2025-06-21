package com.izaiasvalentim.general.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Common.CustomExceptions.UserAlreadyExistsException;
import com.izaiasvalentim.general.Domain.ApiUser;
import com.izaiasvalentim.general.Domain.BaseUser;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserRegisterDTO;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserReturnDTO;
import com.izaiasvalentim.general.Repository.ApiUserRepository;

@Service
public class ApiUserService {

    private final ApiUserRepository apiUserRepository;
    private final BaseUserService baseUserService;

    @Autowired
    public ApiUserService(ApiUserRepository apiUserRepository, BaseUserService baseUserService) {
        this.apiUserRepository = apiUserRepository;
        this.baseUserService = baseUserService;
    }

    public ApiUserReturnDTO registerUser(ApiUserRegisterDTO dto) {
        try {
            validateUserUniqueFields(dto);

            BaseUser baseUser = baseUserService.save(dto);

            ApiUser apiUser = new ApiUser();
            apiUser.setUser(baseUser);
            apiUser.setAddress(dto.address());
            apiUser.setPhone(dto.phone());
            apiUser.setAdmissionDate(dto.admissionDate());
            apiUser.setIsAdmin();
            apiUser.setIsActive(true);
            apiUser.setDeleted(false);
            apiUserRepository.save(apiUser);

            return new ApiUserReturnDTO(apiUser);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }
    }

    public ApiUserReturnDTO updateUser(ApiUserRegisterDTO dto) {
        try {
            ApiUser userToUpdate = apiUserRepository.findByUser(baseUserService.findByUsername(dto.username()))
                    .orElseThrow(() ->
                            new ResourceNotFoundException
                                    ("Erro ao atualizar, nenhum usuário encontrato com os dados fornecidos."));

            if (dto.phone() != null && !dto.phone().isEmpty()) {
                userToUpdate.setPhone(dto.phone());
            }
            if (dto.address() != null && !dto.address().isEmpty()) {
                userToUpdate.setAddress(dto.address());
            }
            apiUserRepository.save(userToUpdate);
            return new ApiUserReturnDTO(userToUpdate);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Erro interno ao atualizar usuário.");
        }
    }

    public ApiUserReturnDTO getUserByUsername(String username) {
        ApiUser user = apiUserRepository.findByUser(baseUserService.findByUsername(username)).orElseThrow(() -> new
                ResourceNotFoundException("Nenhum usuário encontrato com os dados fornecidos."));
        return new ApiUserReturnDTO(user);
    }

    public List<ApiUserReturnDTO> getAllUsers() {
        List<ApiUserReturnDTO> returnDTOs = new ArrayList<>();
        for (ApiUser user : apiUserRepository.findAll()) {
            returnDTOs.add(new ApiUserReturnDTO(user));
        }
        return returnDTOs;
    }

    public void deleteUserByUsername(String username) {
        Optional<ApiUser> apiUser = apiUserRepository.findByUser(baseUserService.findByUsername(username));
        if (apiUser.isEmpty()) {
            throw new ResourceNotFoundException("Erro ao remover, nenhum usuário encontrato com os dados fornecidos.");
        }
        apiUser.get().setIsActive(false);
        apiUser.get().setDeleted(true);
        apiUserRepository.save(apiUser.get());
    }

    private void validateUserUniqueFields(ApiUserRegisterDTO dto) {
        String param = "username";

        BaseUser baseUser = baseUserService.findByUsername(dto.username());
        param = baseUser == null ? "email" : param;

        baseUser = baseUserService.findByEmail(dto.email());
        param = baseUser == null ? "CPF" : param;

        baseUser = baseUserService.findByCPF(dto.CPF());

        if (baseUser != null) {
            throw new UserAlreadyExistsException("Um usuário com este mesmo valor do campo: " + param + ", já existe.");
        }
    }
}
