package com.lmgbgpatr.middleware.dto;

public record Account(int id, int custId, AccountType type, boolean isfrozen, double balance) {}
