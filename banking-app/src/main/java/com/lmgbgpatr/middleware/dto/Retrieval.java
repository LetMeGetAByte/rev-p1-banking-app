package com.lmgbgpatr.middleware.dto;

import java.util.Collection;

public record Retrieval<T>(Class<T> type, Collection<T> data) {}
