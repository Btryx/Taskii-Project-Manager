package com.webfejl.beadando.auth;

public class AuthenticationResponse {
    private String message;

    public AuthenticationResponse(String status) {
        this.message = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String status) {
        this.message = status;
    }
}
