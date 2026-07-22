package ai.synoptiq.auth.service.impl;

import ai.synoptiq.auth.dto.request.LoginRequest;
import ai.synoptiq.auth.dto.request.RegisterRequest;
import ai.synoptiq.auth.dto.response.AuthResponse;
import ai.synoptiq.auth.service.AuthService;
import ai.synoptiq.common.constants.Provider;
import ai.synoptiq.common.constants.Role;
import ai.synoptiq.security.JwtService;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setProvider(Provider.LOCAL);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);

        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtService.generateToken(user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);

        return authResponse;
    }
}
