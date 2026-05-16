package com.marketplace.shop.model;

public class ProductStatus {

    private String productStatuseCode;
    private String state;
    private int productStatuseId;

    public ProductStatus() {
    }

    public ProductStatus(String productStatuseCode, String state, int productStatuseId) {
        this.productStatuseCode = productStatuseCode;
        this.state = state;
        this.productStatuseId = productStatuseId;
    }

    public String getProductStatuseCode() {
        return productStatuseCode;
    }

    public void setProductStatuseCode(String productStatuseCode) {
        this.productStatuseCode = productStatuseCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProductStatuseId() {
        return productStatuseId;
    }

    public void setProductStatuseId(int productStatuseId) {
        this.productStatuseId = productStatuseId;
    }

    @Override
    public String toString() {
        return state;
    }
}
