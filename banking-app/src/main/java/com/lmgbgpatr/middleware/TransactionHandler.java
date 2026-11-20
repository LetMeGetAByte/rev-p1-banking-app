package com.lmgbgpatr.middleware;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TransactionHandler {
   Integer apply(Integer var1, Integer var2, Double var3);
}
