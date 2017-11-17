# Payments schema

# --- !Ups

CREATE TABLE PAYMENTS (
    ID bigint(20) NOT NULL AUTO_INCREMENT,
    NAME varchar(255),
    AMOUNT bigint(20),
    CARD_NO bigint(20),
    EXP_MONTH bigint(20),
    EXP_YEAR bigint(20),
    CVV_NO bigint(20),
    TOKEN varchar(255),
    STATUS varchar(255),
    P_DATE varchar(255),
    FEE bigint(20),
    TOTAL bigint(20),
    TRANS_ID varchar(255),
    PRIMARY KEY (ID)
);

# --- !Downs

DROP TABLE PAYMENTS;
