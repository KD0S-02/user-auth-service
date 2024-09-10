package com.kd0s.user_auth_service.services;

import java.util.List;
import java.util.Optional;

import com.kd0s.user_auth_service.models.UserEntity;

public interface UserService {

    public List<UserEntity> getUsers();

    public Optional<UserEntity> getUser(Long id);

    public UserEntity saveUser(UserEntity user);

    boolean isExists(Long id);

    public UserEntity partialUpdate(UserEntity user, Long id);
}
