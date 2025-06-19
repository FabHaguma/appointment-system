package com.appointmentsystem.controller;

import com.appointmentsystem.dto.UpdateCredentialsRequest;
import com.appointmentsystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/credentials")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'NURSE')")
    public ResponseEntity<?> updateCredentials(Authentication authentication, @RequestBody UpdateCredentialsRequest request) {
        String username = authentication.getName();
        try {
            userService.updateUserCredentials(username, request.getNewUsername(), request.getNewPassword());
            return ResponseEntity.ok("Credentials updated successfully. Please log in again with your new credentials.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
