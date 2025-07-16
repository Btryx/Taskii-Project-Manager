package com.webfejl.beadando.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollaboratorRequest {
    String userId;
    String projectId;
    String role;
}
