package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.Communication;
import com.izaiasvalentim.general.Domain.DTO.Communication.CommunicationUpdateDTO;
import com.izaiasvalentim.general.Service.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/communication")
public class CommunicatonController {

    private final CommunicationService communicationService;

    @Autowired
    public CommunicatonController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @GetMapping("valid-communications")
    public ResponseEntity<?> getlAllValidCommunicatons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(communicationService.getAllValidCommunications(pageable), HttpStatus.OK);
    }

    @GetMapping("all-communications")
    public ResponseEntity<?> getlAllCommunicatons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(communicationService.getAllCommunications(pageable), HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> addNewCommunicaton(@RequestBody Communication communication) {
        return new ResponseEntity<>(communicationService.registerNewCommunication(communication), HttpStatus.CREATED);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> updateCommunicaton(@RequestBody CommunicationUpdateDTO communication) {
        return new ResponseEntity<>(communicationService.updateCommunication(communication), HttpStatus.OK);
    }

    @PutMapping("end-communication/{id}/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> endCommunicaton(@PathVariable int id) {
        return new ResponseEntity<>(communicationService.endCommunication(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> deleteCommunicaton(@PathVariable int id) {
        return new ResponseEntity<>(communicationService.deleteCommunication(id), HttpStatus.OK);
    }
}
