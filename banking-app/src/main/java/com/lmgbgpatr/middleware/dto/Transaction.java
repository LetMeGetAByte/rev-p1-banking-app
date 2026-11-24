package com.lmgbgpatr.middleware.dto;

import java.time.OffsetDateTime;

public record Transaction(int id, int orgID, int destID, double amount, boolean isWithdrawl, OffsetDateTime timestamp) {}