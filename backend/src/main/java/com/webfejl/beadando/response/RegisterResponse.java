package com.webfejl.beadando.response;

import com.webfejl.beadando.entity.User;
import lombok.Getter;

@Getter
public class RegisterResponse {
    private final boolean success;
    private final User user;
    private final String message;

    public RegisterResponse(boolean success, User user, String message) {
        this.success = success;
        this.user = user;
        this.message = message;
    }

}
