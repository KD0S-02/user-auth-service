package com.kd0s.user_auth_service.controllers;

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

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kd0s.user_auth_service.config.auth.TokenProvider;
import com.kd0s.user_auth_service.dtos.JwtDto;
import com.kd0s.user_auth_service.dtos.RefreshTokenDto;
import com.kd0s.user_auth_service.dtos.SignInDto;
import com.kd0s.user_auth_service.dtos.SignUpDto;
import com.kd0s.user_auth_service.models.TokenEntity;
import com.kd0s.user_auth_service.models.UserEntity;
import com.kd0s.user_auth_service.services.AuthService;
import com.kd0s.user_auth_service.services.TokenService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AuthService authService;

    private final TokenProvider tokenProvider;

    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService,
            TokenProvider tokenProvider, TokenService tokenService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.tokenService = tokenService;
    }

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
        UserEntity user = (UserEntity) authUser.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());

        TokenEntity tokenEntity = TokenEntity.builder()
                .username(user.getUsername())
                .token(refreshToken)
                .build();

        tokenService.saveToken(tokenEntity);

        return new ResponseEntity<>(new JwtDto(accessToken, refreshToken), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody @Validated RefreshTokenDto data) {

        String accessToken;

        if (!tokenService.isExists(data.token()))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            String username = tokenProvider.validateToken(data.token());
            accessToken = tokenProvider.generateAccessToken(username);
        } catch (JWTVerificationException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new JwtDto(accessToken, data.token()), HttpStatus.OK);
    }
}
