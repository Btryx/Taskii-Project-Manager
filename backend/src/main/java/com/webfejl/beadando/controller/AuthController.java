package com.webfejl.beadando.controller;


import com.webfejl.beadando.auth.AuthenticationResponse;
import com.webfejl.beadando.auth.LoginRequest;
import com.webfejl.beadando.auth.RegisterResponse;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public RegisterResponse register(@RequestBody User user) {
        User createdUser;
        try {
            createdUser = userService.createUser(user);
        } catch (Exception e) {
            return new RegisterResponse(false, null, e.getMessage());
        }
        return new RegisterResponse(true, createdUser, "Registration successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(new AuthenticationResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}

