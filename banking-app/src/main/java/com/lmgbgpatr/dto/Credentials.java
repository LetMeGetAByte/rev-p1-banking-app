package com.lmgbgpatr.dto;

import java.util.Objects;

public record Credentials(String username, String password, String fname, String lname) {
    public int hash(CredentialsHashMode mode) {
        return switch(mode) {
            case CredentialsHashMode.USERNAME -> username.hashCode();
            case CredentialsHashMode.PASSWORD -> password.hashCode();
        };
    }

    public int hash() {
        return Objects.hash(username(), password());
    }
}