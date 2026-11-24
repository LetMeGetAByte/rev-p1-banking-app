package com.lmgbgpatr.middleware.dto;

import java.time.OffsetDateTime;

public record Transaction(int id, int orgId, int destId, double amount, boolean iswithdral, OffsetDateTime timestamp) {}