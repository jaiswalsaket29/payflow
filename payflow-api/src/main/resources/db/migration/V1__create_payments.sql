CREATE TYPE payment_status AS ENUM ('CREATED','REQUIRES_CUSTOMER_ACTION','AUTHORIZED','CAPTURED','VOIDED','FAILED','REFUNDED');

CREATE TABLE payment(

    id UUID PRIMARY KEY ,
    merchant_id UUID NOT NULL ,
    amount BIGINT NOT NULL CHECK ( amount > 0 ),
    status payment_status NOT NULL ,
    currency VARCHAR(3) NOT NULL ,
    provider_reference VARCHAR ,
    created_at TIMESTAMPTZ NOT NULL ,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL

);

