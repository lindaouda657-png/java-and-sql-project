package com.marketplace.shop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Return {

    private int returnId;
    private String productCodeFk;
    private int orderIdFk;
    private BigDecimal refund;
    private String reason;
    private LocalDate date;

    private String productName;

    public Return() {
    }

    public Return(int returnId, String productCodeFk, int orderIdFk, BigDecimal refund, String reason, LocalDate date) {
        this.returnId = returnId;
        this.productCodeFk = productCodeFk;
        this.orderIdFk = orderIdFk;
        this.refund = refund;
        this.reason = reason;
        this.date = date;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public String getProductCodeFk() {
        return productCodeFk;
    }

    public void setProductCodeFk(String productCodeFk) {
        this.productCodeFk = productCodeFk;
    }

    public int getOrderIdFk() {
        return orderIdFk;
    }

    public void setOrderIdFk(int orderIdFk) {
        this.orderIdFk = orderIdFk;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
