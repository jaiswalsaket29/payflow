package com.payflow.payflow_api.payment.entity;

import com.payflow.payflow_api.common.exception.InvalidStateTransitionException;
import com.payflow.payflow_api.payment.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Payment {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    private UUID merchantId;

    private long amount; // in paise

    private String currency;

    private String providerReference;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "payment_status")
    private PaymentStatus status = PaymentStatus.CREATED;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Version
    private Long version;

    protected Payment() {
        // for JPA only
    }

    public Payment(UUID merchantId, long amount, String currency) {
        if (merchantId == null) {
            throw new IllegalArgumentException("merchantId must not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive (in paise), got " + amount);
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void requireCustomerAction() {
        transitionTo(PaymentStatus.REQUIRES_CUSTOMER_ACTION);
    }

    public void authorize() {
        transitionTo(PaymentStatus.AUTHORIZED);
    }

    public void capture() {
        transitionTo(PaymentStatus.CAPTURED);
    }

    public void voidPayment() {
        transitionTo(PaymentStatus.VOIDED);
    }

    public void refund() {
        transitionTo(PaymentStatus.REFUNDED);
    }

    public void fail() {
        transitionTo(PaymentStatus.FAILED);
    }

    private void transitionTo(PaymentStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new InvalidStateTransitionException(status, next);
        }
        this.status = next;
    }
}