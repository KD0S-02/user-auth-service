package com.kd0s.user_auth_service;

import com.kd0s.user_auth_service.enums.UserRole;
import com.kd0s.user_auth_service.models.UserEntity;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static UserEntity createTestUserA() {
        return UserEntity.builder()
                .username("testUserA")
                .password("asdaere123123d")
                .email("testUserA@mail.com")
                .role(UserRole.USER)
                .build();
    }

    public static UserEntity createTestUserB() {
        return UserEntity.builder()
                .username("testUserB")
                .password("asf[pp[ere123123d")
                .email("testUserB@mail.com")
                .role(UserRole.USER)
                .build();
    }

    public static UserEntity createTestUserC() {
        return UserEntity.builder()
                .username("testUserC")
                .password("asdaere11233123d")
                .email("testUserB@mail.com")
                .role(UserRole.USER)
                .build();
    }
}
