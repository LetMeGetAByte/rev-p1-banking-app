package com.lmgbgpatr.model;

import java.math.BigDecimal;

import com.lmgbgpatr.dto.AccountType;

public record Account(int id, int custID, AccountType type, boolean isFrozen, BigDecimal balance) {}
