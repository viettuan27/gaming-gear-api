package com.tuanviet.gaminggear.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.LoginRequest;
import com.tuanviet.gaminggear.dto.request.RefreshTokenRequest;
import com.tuanviet.gaminggear.dto.request.RegisterRequest;
import com.tuanviet.gaminggear.dto.response.AuthResponse;
import com.tuanviet.gaminggear.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Xác thực",
        description = "Đăng ký, đăng nhập, làm mới token và đăng xuất"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Đăng ký tài khoản khách hàng")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", response));
    }

    @Operation(summary = "Đăng nhập và nhận access token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Đăng nhập thành công", response)
        );
    }

    @Operation(summary = "Làm mới access token")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        AuthResponse response = authService.refreshToken(request);

        return ResponseEntity.ok(
                ApiResponse.success("Làm mới token thành công", response)
        );
    }

    @Operation(summary = "Đăng xuất và vô hiệu hóa refresh token")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>success("Đăng xuất thành công", null)
        );
    }
}
