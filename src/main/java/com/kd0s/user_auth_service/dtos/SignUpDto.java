package com.kd0s.user_auth_service.dtos;

import com.kd0s.user_auth_service.enums.UserRole;

public record SignUpDto(
        String username,
        String password,
        String email,
        UserRole role) {
}
