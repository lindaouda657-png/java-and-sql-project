package com.marketplace.shop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderDetail {

    private String orderDetailsCode;
    private int orderIdPfk;
    private String productCodePfk;
    private BigDecimal totalAmountOfProduct;
    private LocalDate date;

    private String productName;

    public OrderDetail() {
    }

    public OrderDetail(String orderDetailsCode, int orderIdPfk, String productCodePfk,
            BigDecimal totalAmountOfProduct, LocalDate date) {
        this.orderDetailsCode = orderDetailsCode;
        this.orderIdPfk = orderIdPfk;
        this.productCodePfk = productCodePfk;
        this.totalAmountOfProduct = totalAmountOfProduct;
        this.date = date;
    }

    public String getOrderDetailsCode() {
        return orderDetailsCode;
    }

    public void setOrderDetailsCode(String orderDetailsCode) {
        this.orderDetailsCode = orderDetailsCode;
    }

    public int getOrderIdPfk() {
        return orderIdPfk;
    }

    public void setOrderIdPfk(int orderIdPfk) {
        this.orderIdPfk = orderIdPfk;
    }

    public String getProductCodePfk() {
        return productCodePfk;
    }

    public void setProductCodePfk(String productCodePfk) {
        this.productCodePfk = productCodePfk;
    }

    public BigDecimal getTotalAmountOfProduct() {
        return totalAmountOfProduct;
    }

    public void setTotalAmountOfProduct(BigDecimal totalAmountOfProduct) {
        this.totalAmountOfProduct = totalAmountOfProduct;
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
