package com.webfejl.beadando.controller;

import com.webfejl.beadando.response.AuthenticationResponse;
import com.webfejl.beadando.request.LoginRequest;
import com.webfejl.beadando.response.ErrorResponse;
import com.webfejl.beadando.response.RegisterResponse;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Username or password is incorrect!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Login failed. Please try again later."));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserNameById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new AuthenticationResponse(user.getUsername()));
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

}

