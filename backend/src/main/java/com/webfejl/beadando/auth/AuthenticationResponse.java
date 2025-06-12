package com.webfejl.beadando.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponse {

    private String message;

    public AuthenticationResponse(String status) {
        this.message = status;
    }
}
