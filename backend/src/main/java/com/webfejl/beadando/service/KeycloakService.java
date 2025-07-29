package com.webfejl.beadando.service;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class KeycloakService {

    private final String serverUrl = "http://localhost:8080";
    private final String realm = "taskii";
    private final String clientId = "taskii-admin";

    private final String clientSecret = "BvMG7hU76aektoIlRkwYEvYnLXka70Rh";


    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public List<UserRepresentation> getAllUsers() {
        Keycloak keycloak = getKeycloakInstance();
        return keycloak.realm(realm).users().list();
    }

    public UserRepresentation getUserById(String id) {
        Keycloak keycloak = getKeycloakInstance();
        return keycloak.realm(realm).users().get(id).toRepresentation();
    }

    public List<UserRepresentation> getUserByName(String name) {
        if(Objects.equals(name, "admin")) {
            return null;
        }
        Keycloak keycloak = getKeycloakInstance();
        return keycloak.realm(realm).users().search(name);
    }
}
