package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.Client.ClientDTO;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientIdentification;
import com.izaiasvalentim.general.Domain.DTO.Client.ClientRegisterDTO;
import com.izaiasvalentim.general.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegisterDTO clientRegisterDTO) {
        clientService.requestRegistration(clientRegisterDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("approveClient/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> approveClient(@RequestBody ClientIdentification identificationNumber) {
        System.out.println(identificationNumber);
        ClientRegisterDTO clientToReturn = clientService.approveClientRegistration(identificationNumber.identificationNumber());
        return new ResponseEntity<>(clientToReturn, HttpStatus.OK);
    }

    @GetMapping("findByNameAndStatus")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> findClientsByNameAndStatus(@RequestParam String name, @RequestParam Boolean status) {
        List<ClientDTO> returnList = clientService.findClientsByNameAndStatus(name, status);

        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("findByIdentificationNumber")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> findByIdentificationNumber(@RequestParam String identificationNumber) {
        return new ResponseEntity<>(clientService.findByIdentificationNumber(identificationNumber), HttpStatus.OK);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_SELLER')")
    public ResponseEntity<?> updateClient(@RequestBody ClientRegisterDTO clientRegisterDTO) {
        clientService.updateRegistration(clientRegisterDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> deleteClient(@RequestParam String identificationNumber) {
        clientService.logicalDeleteClient(identificationNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
