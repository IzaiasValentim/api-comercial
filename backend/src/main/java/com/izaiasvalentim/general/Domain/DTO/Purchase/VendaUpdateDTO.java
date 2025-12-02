package com.izaiasvalentim.general.Domain.DTO.Purchase;

import java.util.List;

public record VendaUpdateDTO(
        String paymentMethod,
        List<PurchaseItemRequestDTO> items
) {}
