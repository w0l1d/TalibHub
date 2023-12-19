package org.ilisi.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.AuthRequestDTO;
import org.ilisi.backend.services.AuthenticationService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody @Valid AuthRequestDTO appUser) {
        log.info("Received login request for username: {}", appUser.getUsername());
        try {
            // Authentication logic
            Map<String, Object> tokens = authenticationService.authenticate(appUser);
            log.info("Authentication successful for user: {}", appUser.getUsername());
            return tokens;
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", appUser.getUsername(), e);
            throw e; // Rethrow the exception for handling in a global exception handler, if configured.
        }
    }

    @PostMapping("/refreshToken")
    public Map<String, Object> generateRefreshToken(@RequestParam("refreshToken") String refreshToken) {
        log.info("Received refresh token request for token: {}", refreshToken);
        try {
            // Authentication logic
            Map<String, Object> tokens = authenticationService.generateNewAccessToken(refreshToken);
            log.info("Refresh token successful for token: {}", refreshToken);
            return tokens;
        } catch (AuthenticationException e) {
            log.error("Refresh token failed for token: {}", refreshToken, e);
            throw e; // Rethrow the exception for handling in a global exception handler, if configured.
        }
    }

}