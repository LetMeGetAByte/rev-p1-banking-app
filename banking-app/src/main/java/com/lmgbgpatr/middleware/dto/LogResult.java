package com.lmgbgpatr.middleware.dto;

import java.util.Map;

public record LogResult<T>(Map<Integer, Log> results) {}