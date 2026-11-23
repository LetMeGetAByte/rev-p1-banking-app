package com.lmgbgpatr.middleware.dto;

import java.util.Map;

public record TransactionResult<T>(Map<Integer, Transaction> results) {}