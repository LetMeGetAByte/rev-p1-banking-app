package com.lmgbgpatr.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RetrievalResult<T>(Map<Integer, List<T>> results) {

    public int size() {
        return results.size();
    }

    public List<T> get(int key) {
        return results.get(key);
    }

    public List<T> getAll() {
        return
            results()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}