package com.webfejl.beadando.security;

import com.webfejl.beadando.auth.jwt.JwtAuthenticationFilter;
import com.webfejl.beadando.auth.jwt.JwtManager;
import com.webfejl.beadando.config.SecurityConfig;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void jwtAuthenticationFilterBeanIsCreated() {
        JwtManager jwtManager = mock(JwtManager.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        JwtAuthenticationFilter filter = securityConfig.jwtAuthenticationFilter(jwtManager, userDetailsService);

        assertNotNull(filter);
    }

    @Test
    void passwordEncoderBeanIsCreated() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }

    @Test
    void userDetailsServiceLoadsUserCorrectly() {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetailsService uds = securityConfig.userDetailsService(userRepository);
        UserDetails userDetails = uds.loadUserByUsername("john");

        assertEquals("john", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void userDetailsServiceThrowsIfUserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        UserDetailsService uds = securityConfig.userDetailsService(userRepository);

        assertThrows(UsernameNotFoundException.class, () -> uds.loadUserByUsername("notfound"));
    }

    @Test
    void authenticationManagerBeanIsCreated() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);

        when(configuration.getAuthenticationManager()).thenReturn(expectedManager);

        AuthenticationManager actualManager = securityConfig.authenticationManager(configuration);

        assertSame(expectedManager, actualManager);
    }
}

