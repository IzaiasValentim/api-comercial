package com.izaiasvalentim.general.Domain.DTO.Item;

import java.util.Date;

public record ItemDTO(String name, String type, Double price, Double quantity, String code,
                      Date validity, Boolean hasValidity) {
}
