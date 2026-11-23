package com.lmgbgpatr.middleware.dto;

import java.util.Map;

public record CustomerResult<T>(Map<Integer, Customer> results) {}