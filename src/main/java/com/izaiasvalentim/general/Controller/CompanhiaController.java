package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.Companhia;
import com.izaiasvalentim.general.Service.CompanhiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/company")
public class CompanhiaController {

    private final CompanhiaService companhiaService;

    @Autowired
    public CompanhiaController(CompanhiaService companhiaService) {
        this.companhiaService = companhiaService;
    }

    @GetMapping
    public ResponseEntity<?> getCompany() {
        return ResponseEntity.ok(companhiaService.getCompany());
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR')")
    public ResponseEntity<?> addCompany(@RequestBody Companhia companhia) {
        var savedCompany = companhiaService.createCompany(companhia);
        return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR')")
    public ResponseEntity<?> updateCompany(@RequestBody Companhia companhia) {
        return new ResponseEntity<>(companhiaService.updateCompany(companhia), HttpStatus.OK);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR')")
    public ResponseEntity<?> deleteCompany() {
        companhiaService.deleteCompany();
        return ResponseEntity.ok().build();
    }
}
