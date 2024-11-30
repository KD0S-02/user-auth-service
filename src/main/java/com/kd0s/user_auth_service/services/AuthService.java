package com.kd0s.user_auth_service.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kd0s.user_auth_service.dtos.SignUpDto;
import com.kd0s.user_auth_service.models.UserEntity;
import com.kd0s.user_auth_service.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public UserDetails signUp(SignUpDto data) throws Exception {
        if (userRepository.findByUsername(data.username()) != null) {
            throw new Exception("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserEntity newUser = UserEntity.builder()
                .username(data.username())
                .password(encryptedPassword)
                .email(data.email())
                .role(data.role())
                .build();
        return userRepository.save(newUser);
    }

}
