package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.LoginRequest;
import com.tuanviet.gaminggear.dto.request.RefreshTokenRequest;
import com.tuanviet.gaminggear.dto.request.RegisterRequest;
import com.tuanviet.gaminggear.dto.response.AuthResponse;
import com.tuanviet.gaminggear.entity.auth.RefreshToken;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(RefreshTokenRequest request);
}
