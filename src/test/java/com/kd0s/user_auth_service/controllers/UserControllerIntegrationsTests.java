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
import com.kd0s.user_auth_service.TestDataUtil;
import com.kd0s.user_auth_service.models.UserEntity;
import com.kd0s.user_auth_service.services.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationsTests {

    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationsTests(MockMvc mockMvc, UserService userService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Test
    @Transactional
    public void testCreateUserCreatesUserAndReturns201() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        String testUserJson = objectMapper.writeValueAsString(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(testUserJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pwdHash").value(testUser.getPwdHash()));
    }

    @Test
    @Transactional
    public void testListUsersReturnsUsersandReturns200() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        userService.saveUser(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(testUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pwdHash").value(testUser.getPwdHash()));
    }

    @Test
    @Transactional
    public void testGetUserReturnsUsersandReturns200() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        UserEntity savedTestUser = userService.saveUser(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + savedTestUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(savedTestUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pwdHash").value(savedTestUser.getPwdHash()));
    }
}
