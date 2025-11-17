package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.Purchase.PurchaseRequestDTO;
import com.izaiasvalentim.general.Domain.DTO.Purchase.PurchaseResponseDTO;
import com.izaiasvalentim.general.Service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<PurchaseResponseDTO> createPurchase(@Valid @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        PurchaseResponseDTO createdPurchase = vendaService.createPurchase(purchaseRequestDTO);
        return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
    }

}
