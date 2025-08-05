package com.example.accesscontrol.service;

import com.example.accesscontrol.dto.AuthRequest;
import com.example.accesscontrol.dto.AuthResponse;
import com.example.accesscontrol.entity.User;
import com.example.accesscontrol.repository.UserRepository;
import com.example.accesscontrol.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(request.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .role("USER") // just a placeholder
                .build();
    }
}
