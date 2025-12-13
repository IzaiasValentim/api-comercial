package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.Purchase.*;
import com.izaiasvalentim.general.Service.VendaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchases")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_SELLER') || hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<Page<PurchaseListDTO>> getAllPurchases(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(vendaService.findAllPaged(cpf, name, status, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_SELLER') || hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN') ")
    public ResponseEntity<?> getPurchaseById(@PathVariable UUID id) {
        if (id == null) {
            return new ResponseEntity<>("id inválido", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(vendaService.buscarVendaPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_SELLER') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<PurchaseResponseDTO> createPurchase(@Valid @RequestBody PurchaseRequestDTO dto) {
        return new ResponseEntity<>(vendaService.createPurchase(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_SELLER') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<PurchaseResponseDTO> updatePurchase(@PathVariable UUID id, @RequestBody VendaUpdateDTO dto) {
        return ResponseEntity.ok(vendaService.updatePurchase(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SCOPE_SELLER') || hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @RequestBody VendaStatusDTO dto) {
        vendaService.updateStatus(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Void> cancelPurchase(@PathVariable UUID id) {
        vendaService.cancelPurchase(id);
        return ResponseEntity.ok().build();
    }
}
