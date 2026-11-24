package com.lmgbgpatr.service.handlers;

import com.lmgbgpatr.dto.UserLevel;

public interface RegistrationHandler {
    UserLevel apply(String username, String firstName, String lastName, int hash);
}
