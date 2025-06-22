package com.webfejl.beadando.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.webfejl.beadando.auth.LoginRequest;
import com.webfejl.beadando.auth.jwt.JwtManager;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtManager jwtManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldEncodePasswordAndSaveUser() throws Exception {
        User user = new User();
        user.setPassword("plainPassword");

        User savedUser = new User();
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(user);

        // Verify password was encoded and set
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(user);
    }

    @Test
    void login_shouldReturnToken_whenAuthenticationSucceeds() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("pass");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);
        when(jwtManager.generateToken("user")).thenReturn("jwt-token");

        String token = userService.login(loginRequest);

        assertEquals("jwt-token", token);
        verify(authenticationManager).authenticate(any());
        verify(jwtManager).generateToken("user");
    }

    @Test
    void login_shouldReturnNull_whenAuthenticationFails() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("wrongpass");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(false);

        String token = userService.login(loginRequest);

        assertNull(token);
        verify(authenticationManager).authenticate(any());
        verify(jwtManager, never()).generateToken(any());
    }

    @Test
    void login_shouldThrowException_whenAuthenticationThrows() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("pass");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad creds"));

        assertThrows(BadCredentialsException.class, () -> {
            userService.login(loginRequest);
        });

        verify(authenticationManager).authenticate(any());
        verify(jwtManager, never()).generateToken(any());
    }
}

