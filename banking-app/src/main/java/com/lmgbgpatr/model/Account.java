package com.lmgbgpatr.model;

import com.lmgbgpatr.dto.AccountType;

public record Account(int id, int custID, AccountType type, boolean isFrozen, double balance) {}
