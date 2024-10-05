package com.kd0s.user_auth_service.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kd0s.user_auth_service.dtos.UserDto;
import com.kd0s.user_auth_service.mappers.Mapper;
import com.kd0s.user_auth_service.models.UserEntity;
import com.kd0s.user_auth_service.services.UserService;

import lombok.extern.java.Log;

@RestController
@Log
public class UserController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/users")
    public List<UserDto> listUsers() {
        List<UserEntity> users = userService.getUsers();

        return users.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.getUser(id);

        return foundUser.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/users")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto user) {
        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity savedUserEntity = userService.saveUser(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> fullUpdateUser(@PathVariable("id") Long id, @RequestBody UserDto user) {

        if (!userService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserEntity userEntity = userMapper.mapFrom(user);
        userService.saveUser(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(userEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(@PathVariable("id") Long id, @RequestBody UserDto user) {

        if (!userService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity updatedUser = userService.partialUpdate(userEntity, id);

        return new ResponseEntity<>(userMapper.mapTo(updatedUser), HttpStatus.OK);
    }
}
