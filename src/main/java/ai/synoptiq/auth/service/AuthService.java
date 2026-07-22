package ai.synoptiq.auth.service;

import ai.synoptiq.auth.dto.request.LoginRequest;
import ai.synoptiq.auth.dto.request.RegisterRequest;
import ai.synoptiq.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
