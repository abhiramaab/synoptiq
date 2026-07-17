package ai.synoptiq.auth.service.impl;

import ai.synoptiq.auth.dto.request.LoginRequest;
import ai.synoptiq.auth.dto.request.RegisterRequest;
import ai.synoptiq.auth.dto.response.AuthResponse;
import ai.synoptiq.auth.service.AuthService;
import ai.synoptiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
