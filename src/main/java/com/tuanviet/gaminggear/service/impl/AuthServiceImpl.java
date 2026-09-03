package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.LoginRequest;
import com.tuanviet.gaminggear.dto.request.RefreshTokenRequest;
import com.tuanviet.gaminggear.dto.request.RegisterRequest;
import com.tuanviet.gaminggear.dto.response.AuthResponse;
import com.tuanviet.gaminggear.entity.auth.RefreshToken;
import com.tuanviet.gaminggear.entity.auth.Role;
import com.tuanviet.gaminggear.entity.auth.RoleName;
import com.tuanviet.gaminggear.entity.auth.User;
import com.tuanviet.gaminggear.exception.ConflictException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.exception.UnauthorizedException;
import com.tuanviet.gaminggear.repository.RefreshTokenRepository;
import com.tuanviet.gaminggear.repository.RoleRepository;
import com.tuanviet.gaminggear.repository.UserRepository;
import com.tuanviet.gaminggear.security.jwt.JwtService;
import com.tuanviet.gaminggear.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email đã được sử dụng");
        }

        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quyền CUSTOMER"));

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setActive(true);
        user.getRoles().add(customerRole);

        userRepository.save(user);

        return createAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password())
            );
        } catch (AuthenticationException exception) {
            throw new UnauthorizedException("Email hoặc mật khẩu không đúng");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        return createAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (!jwtService.isValid(token) || !jwtService.isRefreshToken(token)) {
            throw new UnauthorizedException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        UUID jti = jwtService.extractJti(token);

        RefreshToken refreshToken = refreshTokenRepository
                .findByJtiAndRevokedFalseAndExpiresAtAfter(jti, Instant.now())
                .orElseThrow(() -> new UnauthorizedException("Refresh token không còn hiệu lực"));

        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);

        return createAuthResponse(refreshToken.getUser());
    }

    @Override
    public void logout(RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (!jwtService.isValid(token) || !jwtService.isRefreshToken(token)) {
            throw new UnauthorizedException("Refresh token không hợp lệ");
        }

        UUID jti = jwtService.extractJti(token);

        RefreshToken refreshToken = refreshTokenRepository
                .findByJtiAndRevokedFalseAndExpiresAtAfter(jti, Instant.now())
                .orElseThrow(() -> new UnauthorizedException("Refresh token không còn hiệu lực"));

        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(Instant.now());

        refreshTokenRepository.save(refreshToken);
    }

    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);

        UUID jti = UUID.randomUUID();
        String refreshTokenValue = jwtService.generateRefreshToken(user, jti);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setJti(jti);
        refreshToken.setExpiresAt(
                Instant.now().plusMillis(jwtService.getRefreshTokenExpiration())
        );
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                refreshTokenValue,
                "Bearer",
                jwtService.getAccessTokenExpiration()
        );
    }
}
