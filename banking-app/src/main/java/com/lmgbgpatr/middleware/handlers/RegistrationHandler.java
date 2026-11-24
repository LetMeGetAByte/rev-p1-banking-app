package com.lmgbgpatr.middleware.handlers;

import com.lmgbgpatr.middleware.dto.UserLevel;

public interface RegistrationHandler {
    UserLevel apply(String username, String firstName, String lastName, int hash);
}
