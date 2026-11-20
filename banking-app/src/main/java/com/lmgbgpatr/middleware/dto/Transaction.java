package com.lmgbgpatr.middleware.dto;

public record Account(int id, int orgId, int destId, double amount, LocalDateTime timestamp) {}