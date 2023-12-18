package org.ilisi.backend.services;

import lombok.RequiredArgsConstructor;
import org.ilisi.backend.dto.AuthRequestDTO;
import org.ilisi.backend.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Map<String, Object> authenticate(AuthRequestDTO appUser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        return Map.of(
                "access-token", jwtService.generateAccessToken(user),
                "refresh-token", jwtService.generateRefreshToken(user)
        );
    }


}