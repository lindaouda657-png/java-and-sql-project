package com.marketplace.shop.model;

public class Order {

    private int orderId;
    private int customerIdFk;
    private int userIdFk;
    private int orderStatuseIdFk;

    private String customerName;
    private String employeeName;
    private String statusState;

    public Order() {
    }

    public Order(int orderId, int customerIdFk, int userIdFk, int orderStatuseIdFk) {
        this.orderId = orderId;
        this.customerIdFk = customerIdFk;
        this.userIdFk = userIdFk;
        this.orderStatuseIdFk = orderStatuseIdFk;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerIdFk() {
        return customerIdFk;
    }

    public void setCustomerIdFk(int customerIdFk) {
        this.customerIdFk = customerIdFk;
    }

    public int getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(int userIdFk) {
        this.userIdFk = userIdFk;
    }

    public int getOrderStatuseIdFk() {
        return orderStatuseIdFk;
    }

    public void setOrderStatuseIdFk(int orderStatuseIdFk) {
        this.orderStatuseIdFk = orderStatuseIdFk;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStatusState() {
        return statusState;
    }

    public void setStatusState(String statusState) {
        this.statusState = statusState;
    }

    @Override
    public String toString() {
        return "Order #" + orderId;
    }
}
