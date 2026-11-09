package com.lmgbgpatr.ui.states;

import com.lmgbgpatr.middleware.dto.Credentials;
import com.lmgbgpatr.middleware.dto.UserLevel;

public class UserState {

    private static final UserState INSTANCE = new UserState();

    private Credentials credentials;
    private UserLevel level = UserLevel.UNAUTHORIZED;

    public static UserState instance() {
        return INSTANCE;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }

    public Credentials credentials() {
        return credentials;
    }

    public String username() {
        return credentials.username();
    }

    public String password() {
        return credentials.password();
    }

    public Integer hash() {
        return credentials.hash();
    }

    public Boolean authorized() {
        return !level.equals(UserLevel.UNAUTHORIZED);
    }

    public UserLevel level() {
        return level;
    }
}
