package com.lmgbgpatr.middleware.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AccountsResult(Map<Integer, List<Account>> result) {
    
    public List<Account> get(int hash) {
        return result.get(hash);
    }

    public List<Account> getAll() {
        return
            result()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}