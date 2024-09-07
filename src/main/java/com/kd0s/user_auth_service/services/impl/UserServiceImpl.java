package com.kd0s.user_auth_service.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.kd0s.user_auth_service.models.UserEntity;
import com.kd0s.user_auth_service.repositories.UserRepository;
import com.kd0s.user_auth_service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> getUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isExists(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserEntity partialUpdate(UserEntity user, UUID id) {
        user.setId(id);

        return userRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(user.getUsername()).ifPresent(existingUser::setUsername);
            Optional.ofNullable(user.getPwdHash()).ifPresent(existingUser::setPwdHash);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("user does not exist"));
    }

}
