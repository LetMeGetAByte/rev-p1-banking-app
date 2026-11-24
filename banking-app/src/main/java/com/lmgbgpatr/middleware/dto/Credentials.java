package com.lmgbgpatr.middleware.dto;

public record Credentials(String username, String password, String fname, String lname) {
    public int hash(CredentialsHashMode mode) {
        return switch(mode) {
            case CredentialsHashMode.USERNAME -> username.hashCode();
            case CredentialsHashMode.PASSWORD -> password.hashCode();
            case CredentialsHashMode.FIRSTNAME -> fname.hashCode();
            case CredentialsHashMode.LASTNAME -> lname.hashCode();
        };
    }

    public int hash() {
        return this.hashCode();
    }
}