package com.webfejl.beadando.controller;

import com.webfejl.beadando.request.LoginRequest;
import com.webfejl.beadando.auth.jwt.JwtManager;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtManager jwtManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void registerShouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userService.createUser(any(User.class))).thenReturn(user);

        String registerJson = """
            {
                "username": "testuser",
                "password": "password123",
                "enabled": true
            }
            """;

        mockMvc.perform(post("http://localhost:8082/api/register")
                        .contentType("application/json")
                        .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        String token = "dummy-jwt-token";

        when(userService.login(any(LoginRequest.class))).thenReturn(token);


        String loginJson = """
            {
                "username": "testuser",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(token));
    }

    @Test
    void loginShouldReturnUnauthorizedOnFailure() throws Exception {
        when(userService.login(any(LoginRequest.class))).thenReturn(null);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"username": "wronguser", "password": "wrongpass"}
                """))
                .andExpect(status().isUnauthorized());
    }
}
