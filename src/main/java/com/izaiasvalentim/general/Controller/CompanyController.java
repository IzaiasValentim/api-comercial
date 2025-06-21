package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.Company;
import com.izaiasvalentim.general.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<?> getCompany() {
        return ResponseEntity.ok(companyService.getCompany());
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR')")
    public ResponseEntity<?> addCompany(@RequestBody Company company) {
        var savedCompany = companyService.createCompany(company);
        return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR')")
    public ResponseEntity<?> updateCompany(@RequestBody Company company) {
        return new ResponseEntity<>(companyService.updateCompany(company), HttpStatus.OK);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR')")
    public ResponseEntity<?> deleteCompany() {
        companyService.deleteCompany();
        return ResponseEntity.ok().build();
    }
}
