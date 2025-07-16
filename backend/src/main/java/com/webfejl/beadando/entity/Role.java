package com.webfejl.beadando.entity;

public enum Role {
    ADMIN("Admin"),
    CONTRIBUTOR("Contributor"),
    VIEWER("Viewer");

    Role(String label) {
        this.label = label;
    }

    final String label;
}
