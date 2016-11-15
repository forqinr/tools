package com.zhao.jdbc;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 对应TB_CONTRACT_TAX表
 *
 * @author zhaoyan
 * @since 2016.11.15 10:33
 */
public class TaxBean {
    private String ID;
    private String TAX_ID;
    private String CONTRACT_ID;
    private BigDecimal TAX_AMOUNT;
    private String TAX_PERSON;
    private Timestamp TAX_PAY_TIME;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTAX_ID() {
        return TAX_ID;
    }

    public void setTAX_ID(String TAX_ID) {
        this.TAX_ID = TAX_ID;
    }

    public String getCONTRACT_ID() {
        return CONTRACT_ID;
    }

    public void setCONTRACT_ID(String CONTRACT_ID) {
        this.CONTRACT_ID = CONTRACT_ID;
    }

    public BigDecimal getTAX_AMOUNT() {
        return TAX_AMOUNT;
    }

    public void setTAX_AMOUNT(BigDecimal TAX_AMOUNT) {
        this.TAX_AMOUNT = TAX_AMOUNT;
    }

    public String getTAX_PERSON() {
        return TAX_PERSON;
    }

    public void setTAX_PERSON(String TAX_PERSON) {
        this.TAX_PERSON = TAX_PERSON;
    }

    public Timestamp getTAX_PAY_TIME() {
        return TAX_PAY_TIME;
    }

    public void setTAX_PAY_TIME(Timestamp TAX_PAY_TIME) {
        this.TAX_PAY_TIME = TAX_PAY_TIME;
    }
}
