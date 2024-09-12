package com.kd0s.user_auth_service.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd0s.user_auth_service.dtos.SignInDto;
import com.kd0s.user_auth_service.dtos.SignUpDto;
import com.kd0s.user_auth_service.enums.UserRole;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class authControllerIntegrationsTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public authControllerIntegrationsTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @Transactional
    public void testThatSignUpWhenSuccessfulReturns201() throws Exception {
        SignUpDto userData = new SignUpDto("kd0s", "password#123", "test@mail.com", UserRole.USER);
        String userDataJson = objectMapper.writeValueAsString(userData);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(userDataJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Transactional
    public void testThatSignInWorks() throws Exception {
        SignUpDto userData = new SignUpDto("kd0s", "password#123", "test@mail.com", UserRole.USER);
        String userDataJson = objectMapper.writeValueAsString(userData);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(userDataJson));
        SignInDto signInDto = new SignInDto("kd0s", "password#123");
        String signInDtoJson = objectMapper.writeValueAsString(signInDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signin").contentType(MediaType.APPLICATION_JSON)
                        .content(signInDtoJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isString());
    }
}
