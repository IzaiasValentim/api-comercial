package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserRegisterDTO;
import com.izaiasvalentim.general.Domain.DTO.ApiUser.ApiUserReturnDTO;
import com.izaiasvalentim.general.Service.ApiUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/users")
public class ApiUserController {

    private final ApiUserService apiUserService;

    public ApiUserController(ApiUserService apiUserService) {
        this.apiUserService = apiUserService;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> createUser(@RequestBody ApiUserRegisterDTO dto) {
        ApiUserReturnDTO userSaved = apiUserService.registerUser(dto);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> updateUser(@RequestBody ApiUserRegisterDTO dto) {
        ApiUserReturnDTO userUpdated = apiUserService.updateUser(dto);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    // --> Temporary Methods.
    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        ApiUserReturnDTO apiUser = apiUserService.getUserByUsername(username);

        return new ResponseEntity<>(apiUser, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> getAllUsers() {
        var allUsers = apiUserService.getAllUsers();

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
    // <--

    @DeleteMapping()
    @PreAuthorize("hasAuthority('SCOPE_ADMINISTRATOR') || hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<?> deleteUserByUsername(@RequestParam String username) {
        apiUserService.deleteUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
