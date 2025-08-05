package com.example.accesscontrol.service;

import com.example.accesscontrol.dto.AuthRequest;
import com.example.accesscontrol.dto.AuthResponse;
import com.example.accesscontrol.entity.User;
import com.example.accesscontrol.exception.*;
import com.example.accesscontrol.repository.UserRepository;
import com.example.accesscontrol.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isEnabled()) {
            throw new UserDisabledException();
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .role("USER") // Replace when role logic is ready
                .build();
    }
}
