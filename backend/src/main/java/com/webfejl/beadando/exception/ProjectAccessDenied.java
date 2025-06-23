package com.webfejl.beadando.exception;

public class ProjectAccessDenied extends RuntimeException {

    public ProjectAccessDenied(String message) {
        super(message);
    }
}