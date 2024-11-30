package com.kd0s.user_auth_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kd0s.user_auth_service.models.TokenEntity;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByUsername(String username);

    Optional<TokenEntity> findByToken(String token);

    Boolean existsByToken(String token);

    void deleteByToken(String token);

}
