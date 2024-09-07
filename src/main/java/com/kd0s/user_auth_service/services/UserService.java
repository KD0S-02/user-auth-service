package com.kd0s.user_auth_service.services;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import com.kd0s.user_auth_service.models.UserEntity;

public interface UserService {

    public List<UserEntity> getUsers();

    public Optional<UserEntity> getUser(UUID id);

    public UserEntity saveUser(UserEntity user);

    boolean isExists(UUID id);

    public UserEntity partialUpdate(UserEntity user, UUID id);
}
