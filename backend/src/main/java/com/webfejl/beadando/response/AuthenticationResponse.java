package com.webfejl.beadando.response;

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
