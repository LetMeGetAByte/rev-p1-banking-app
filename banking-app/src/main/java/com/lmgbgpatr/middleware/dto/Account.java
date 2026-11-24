package com.lmgbgpatr.middleware.dto;

public record Account(int id, int custID, AccountType type, boolean isFrozen, double balance) {}
