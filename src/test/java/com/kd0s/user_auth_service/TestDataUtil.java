package com.kd0s.user_auth_service;

import com.kd0s.user_auth_service.models.UserEntity;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static UserEntity createTestUserA() {
        return UserEntity.builder()
                .username("testUserA")
                .pwdHash("asdaere123123d")
                .build();
    }

    public static UserEntity createTestUserB() {
        return UserEntity.builder()
                .username("testUserB")
                .pwdHash("asf[pp[ere123123d")
                .build();
    }

    public static UserEntity createTestUserC() {
        return UserEntity.builder()
                .username("testUserC")
                .pwdHash("asdaere11233123d")
                .build();
    }
}
