package com.kd0s.user_auth_service.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kd0s.user_auth_service.models.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

}
