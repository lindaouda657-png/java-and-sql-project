package com.marketplace.shop.model;

import java.math.BigDecimal;

public class Payment {

    private int paymentId;
    private int orderIdFk;
    private BigDecimal total;
    private String paymentMethod;
    private int customerId;

    private String employeeName;

    public Payment() {
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderIdFk() {
        return orderIdFk;
    }

    public void setOrderIdFk(int orderIdFk) {
        this.orderIdFk = orderIdFk;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserIdFk() {
        return customerId;
    }

    public void setUserIdFk(int id) {
        this.customerId = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
