package com.kd0s.user_auth_service.enums;

public enum UserRole {
    GUEST("guest"),
    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }
}
