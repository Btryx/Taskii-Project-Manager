package com.webfejl.beadando.response;

import lombok.Getter;
import org.keycloak.representations.idm.UserRepresentation;

@Getter
public class RegisterResponse {
    private final boolean success;
    private final UserRepresentation user;
    private final String message;

    public RegisterResponse(boolean success, UserRepresentation user, String message) {
        this.success = success;
        this.user = user;
        this.message = message;
    }

}
