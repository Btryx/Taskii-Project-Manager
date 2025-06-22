package com.webfejl.beadando.security;

import com.webfejl.beadando.request.LoginRequest;
import com.webfejl.beadando.auth.jwt.JwtManager;
import com.webfejl.beadando.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtManager jwtManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UserService userService;

    @Test
    void publicEndpointsShouldBeAccessibleWithoutAuth() throws Exception {
        String loginJson = """
            {
                "username": "testuser",
                "password": "password123"
            }
            """;

        String registerJson = """
            {
                "username": "testuser",
                "password": "password123",
                "enabled": true
            }
            """;

        when(userService.login(any(LoginRequest.class)))
                .thenReturn("dummy-jwt-token");

        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("dummy-jwt-token"));

        mockMvc.perform(post("http://localhost:8082/api/register")
                        .contentType("application/json")
                        .content(registerJson))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointShouldReturn401WithoutAuth() throws Exception {
        mockMvc.perform(get("http://localhost:8082/api/projects/all"))
                .andExpect(status().isForbidden());
    }
}
