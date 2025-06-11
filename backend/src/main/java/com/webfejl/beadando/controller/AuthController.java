package com.webfejl.beadando.controller;


import com.webfejl.beadando.auth.AuthenticationResponse;
import com.webfejl.beadando.auth.LoginRequest;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(produces = "application/json")
    @RequestMapping({ "/validateLogin" })
    public ResponseEntity<AuthenticationResponse> validateLogin() {
        return ResponseEntity.ok(new AuthenticationResponse("User successfully authenticated"));
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        //todo: use jwt token
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(new AuthenticationResponse("Login successful"));
    }

}

