package com.payflow.payflow_api.payment;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;


public enum PaymentStatus {
    CREATED,
    REQUIRES_CUSTOMER_ACTION,
    AUTHORIZED,
    CAPTURED,
    FAILED,
    VOIDED,
    REFUNDED;

    private static final Map<PaymentStatus, Set<PaymentStatus>> ALLOWED = new EnumMap<>(PaymentStatus.class);


    static {
        ALLOWED.put(CREATED, EnumSet.of(REQUIRES_CUSTOMER_ACTION, AUTHORIZED, FAILED));
        ALLOWED.put(REQUIRES_CUSTOMER_ACTION, EnumSet.of(AUTHORIZED, FAILED));
        ALLOWED.put(AUTHORIZED, EnumSet.of(CAPTURED, VOIDED));
        ALLOWED.put(CAPTURED, EnumSet.of(REFUNDED));
        ALLOWED.put(VOIDED, EnumSet.noneOf(PaymentStatus.class));
        ALLOWED.put(REFUNDED, EnumSet.noneOf(PaymentStatus.class));
        ALLOWED.put(FAILED, EnumSet.noneOf(PaymentStatus.class));
    }

    public boolean canTransitionTo(PaymentStatus next) {
        return ALLOWED.get(this).contains(next);
    }

    public boolean isTerminal(){
        return ALLOWED.get(this).isEmpty();
    }

}

