package com.lmgbgpatr.middleware.dto;

public record Credentials(String username, String password) {
    public int hash(CredentialsHashMode mode) {
        return switch(mode) {
            case CredentialsHashMode.USERNAME -> username.hashCode();
            case CredentialsHashMode.PASSWORD -> password.hashCode();
        };
    }

    public int hash() {
        return this.hashCode();
    }
}