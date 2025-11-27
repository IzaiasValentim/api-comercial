package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.Client.ClientDTO;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientIdentification;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientRegisterDTO;
import com.izaiasvalentim.general.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clients")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/")
    @PreAuthorize(" hasAuthority('SCOPE_MANAGER') ||hasAuthority('SCOPE_SELLER') ")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegisterDTO clientRegisterDTO) {
        clienteService.requestRegistration(clientRegisterDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("approveClient/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> approveClient(@RequestBody ClientIdentification identificationNumber) {
        System.out.println(identificationNumber);
        ClientRegisterDTO clientToReturn = clienteService.approveClientRegistration(identificationNumber.identificationNumber());
        return new ResponseEntity<>(clientToReturn, HttpStatus.OK);
    }

    @GetMapping("findByNameAndStatus")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> findClientsByNameAndStatus(@RequestParam String name, @RequestParam Boolean status) {
        List<ClientDTO> returnList = clienteService.findClientsByNameAndStatus(name, status);

        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("findByIdentificationNumber")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> findByIdentificationNumber(@RequestParam String identificationNumber) {
        return new ResponseEntity<>(clienteService.findByIdentificationNumber(identificationNumber), HttpStatus.OK);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> updateClient(@RequestBody ClientRegisterDTO clientRegisterDTO) {
        clienteService.updateRegistration(clientRegisterDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> deleteClient(@RequestParam String identificationNumber) {
        clienteService.logicalDeleteClient(identificationNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
