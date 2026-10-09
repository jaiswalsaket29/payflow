package com.payflow.payflow_api.common.exception;

import com.payflow.payflow_api.payment.PaymentStatus;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(PaymentStatus status, PaymentStatus next) {
        super("Cannot transition payment from " + status + " to" + next);
    }
}
