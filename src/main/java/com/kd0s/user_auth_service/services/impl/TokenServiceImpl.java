package com.kd0s.user_auth_service.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.kd0s.user_auth_service.models.TokenEntity;
import com.kd0s.user_auth_service.repositories.TokenRepository;
import com.kd0s.user_auth_service.services.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<TokenEntity> getTokens() {
        return StreamSupport.stream(tokenRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TokenEntity> getToken(String username) {
        return tokenRepository.findByUsername(username);
    }

    @Override
    public Optional<TokenEntity> getUser(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public TokenEntity saveToken(TokenEntity token) {
        return tokenRepository.save(token);
    }

    @Override
    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    @Override
    public boolean isExists(String token) {
        return tokenRepository.existsByToken(token);
    }
}
