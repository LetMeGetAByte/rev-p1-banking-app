package com.lmgbgpatr.dto;

public enum UserLevel { UNAUTHORIZED, CUSTOMER, EMPLOYEE;

    public static UserLevel valueOf(int i) {
        return switch(i) {
            case 1 -> UserLevel.CUSTOMER;
            case 2 -> UserLevel.EMPLOYEE;
            default -> UserLevel.UNAUTHORIZED;
        };
    }
 }