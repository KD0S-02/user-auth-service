package com.kd0s.user_auth_service.services;

import java.util.List;
import java.util.Optional;

import com.kd0s.user_auth_service.models.TokenEntity;

public interface TokenService {

    public boolean isExists(String token);

    public Optional<TokenEntity> getToken(String username);

    public Optional<TokenEntity> getUser(String token);

    public List<TokenEntity> getTokens();

    public TokenEntity saveToken(TokenEntity token);

    public void deleteToken(String token);

}
