package com.kd0s.user_auth_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kd0s.user_auth_service.config.auth.TokenProvider;
import com.kd0s.user_auth_service.dtos.JwtDto;
import com.kd0s.user_auth_service.dtos.SignInDto;
import com.kd0s.user_auth_service.dtos.SignUpDto;
import com.kd0s.user_auth_service.models.UserEntity;
import com.kd0s.user_auth_service.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Validated SignUpDto data) throws Exception {
        authService.signUp(data);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Validated SignInDto data) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.username(),
                data.password());

        Authentication authUser = authenticationManager.authenticate(usernamePassword);

        String accessToken = tokenProvider.generateAccessToken((UserEntity) authUser.getPrincipal());

        return new ResponseEntity<>(new JwtDto(accessToken), HttpStatus.OK);
    }
}
