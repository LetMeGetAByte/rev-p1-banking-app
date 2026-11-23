package com.lmgbgpatr.middleware.dto;

public record Log(int id, String sub, String desc, String[] tags, OffsetDateTime timestamp) {}