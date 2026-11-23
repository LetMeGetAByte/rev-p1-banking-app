package com.lmgbgpatr.middleware.dto;

import java.util.Collection;
import java.util.Map;

public record RetrievalResult<T>(Map<Integer, Collection<T>> results) {}