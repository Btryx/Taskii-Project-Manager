package com.webfejl.beadando.controller;


import com.webfejl.beadando.auth.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @GetMapping(produces = "application/json")
    @RequestMapping({ "/validateLogin" })
    public ResponseEntity<AuthenticationResponse> validateLogin() {
        return ResponseEntity.ok(new AuthenticationResponse("User successfully authenticated"));
    }
}

