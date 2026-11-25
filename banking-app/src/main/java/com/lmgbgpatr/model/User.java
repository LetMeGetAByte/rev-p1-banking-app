package com.lmgbgpatr.model;

import com.lmgbgpatr.dto.UserLevel;

public record User(int id, String username, String password, String fname, String lname, UserLevel level) {}
