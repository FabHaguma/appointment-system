package com.appointmentsystem.controller;

import com.appointmentsystem.security.CustomUserDetails;
import com.appointmentsystem.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Map<String, String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));

        if (authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("role", userDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(",")));
            response.put("actualName", userDetails.getActualName());

            return response;
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    // A simple record DTO for the login request
    public record AuthRequest(String username, String password) {}
}