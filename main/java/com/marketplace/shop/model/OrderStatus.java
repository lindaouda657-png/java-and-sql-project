package com.marketplace.shop.model;

public class OrderStatus {

    private String orderStatuseCode;
    private String state;
    private int orderStatuseId;

    public OrderStatus() {
    }

    public OrderStatus(String orderStatuseCode, String state, int orderStatuseId) {
        this.orderStatuseCode = orderStatuseCode;
        this.state = state;
        this.orderStatuseId = orderStatuseId;
    }

    public String getOrderStatuseCode() {
        return orderStatuseCode;
    }

    public void setOrderStatuseCode(String orderStatuseCode) {
        this.orderStatuseCode = orderStatuseCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getOrderStatuseId() {
        return orderStatuseId;
    }

    public void setOrderStatuseId(int orderStatuseId) {
        this.orderStatuseId = orderStatuseId;
    }

    @Override
    public String toString() {
        return state;
    }
}
