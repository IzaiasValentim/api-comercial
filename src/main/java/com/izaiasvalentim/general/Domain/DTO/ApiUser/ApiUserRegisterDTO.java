package com.izaiasvalentim.general.Domain.DTO.ApiUser;

import java.time.LocalDate;

public record ApiUserRegisterDTO(String username, String CPF, String email,String password, String phone, String address,
                                 int role, LocalDate admissionDate) {
}

