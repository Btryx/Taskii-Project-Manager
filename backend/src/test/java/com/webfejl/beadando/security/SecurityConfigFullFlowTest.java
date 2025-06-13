package com.webfejl.beadando.security;

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
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigFullFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtManager jwtManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void fullRegisterLoginAndAccessProtectedEndpointFlow() throws Exception {
        // Sample user data
        String username = "testuser";
        String password = "password123";
        String dummyToken = "dummy-jwt-token";

        // JSON bodies
        String registerJson = String.format("""
            {
                "username": "%s",
                "password": "%s",
                "enabled": true
            }
            """, username, password);

        String loginJson = String.format("""
            {
                "username": "%s",
                "password": "%s"
            }
            """, username, password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);

        // 1. Mock register to succeed
        when(userService.createUser(any())).thenReturn(user);

        // 2. Mock login to return a token
        when(userService.login(any())).thenReturn(dummyToken);

        // 3. Mock JwtManager to validate token successfully
        when(jwtManager.validateToken(dummyToken, username)).thenReturn(true);
        when(jwtManager.extractUsername(dummyToken)).thenReturn(username);

        // 4. Mock userDetailsService to return a valid user
        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(org.springframework.security.core.userdetails.User
                        .withUsername(username)
                        .password(password)
                        .roles("USER")
                        .build());

        // --- 1. Register ---
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        // --- 2. Login ---
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(dummyToken))
                .andReturn();

        // --- 3. Access protected endpoint with JWT ---
        mockMvc.perform(get("http://localhost:8082/api/projects/all")
                        .header("Authorization", "Bearer " + dummyToken))
                .andExpect(status().isOk());
    }
}
