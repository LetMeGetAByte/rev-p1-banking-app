package com.lmgbgpatr.middleware.dto;

import java.util.Map;

public record RetrievalResult<T>(Map<Integer, Retrieval<T>> results) {}