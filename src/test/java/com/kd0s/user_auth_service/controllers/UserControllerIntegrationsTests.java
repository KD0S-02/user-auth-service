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
import lombok.extern.java.Log;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Log
public class UserControllerIntegrationsTests {

    private UserService userService;

    private MockMvc mockMvc;

    @Autowired
    public UserControllerIntegrationsTests(MockMvc mockMvc, UserService userService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    @Test
    @Transactional
    public void testListUsersReturnsUsersAndReturns200() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        userService.saveUser(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(testUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value(testUser.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(testUser.getEmail()));
    }

    @Test
    @Transactional
    public void testGetUserReturnsUsersAndReturns200() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        UserEntity savedTestUser = userService.saveUser(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + savedTestUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(savedTestUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(savedTestUser.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testUser.getEmail()));
    }
}
