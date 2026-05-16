package com.marketplace.shop.model;

import java.math.BigDecimal;

public class Product {

    private String productCode;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private int categoryIdFk;
    private int productStatuseIdFk;
    private String discountsCodeFk;

    private String categoryName;
    private String statusState;
    private String imageUrl;
    private String description;

    public Product() {
    }

    public Product(String productCode, String productName, BigDecimal unitPrice, int quantity,
            int categoryIdFk, int productStatuseIdFk, String discountsCodeFk) {
        this.productCode = productCode;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.categoryIdFk = categoryIdFk;
        this.productStatuseIdFk = productStatuseIdFk;
        this.discountsCodeFk = discountsCodeFk;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryIdFk() {
        return categoryIdFk;
    }

    public void setCategoryIdFk(int categoryIdFk) {
        this.categoryIdFk = categoryIdFk;
    }

    public int getProductStatuseIdFk() {
        return productStatuseIdFk;
    }

    public void setProductStatuseIdFk(int productStatuseIdFk) {
        this.productStatuseIdFk = productStatuseIdFk;
    }

    public String getDiscountsCodeFk() {
        return discountsCodeFk;
    }

    public void setDiscountsCodeFk(String discountsCodeFk) {
        this.discountsCodeFk = discountsCodeFk;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatusState() {
        return statusState;
    }

    public void setStatusState(String statusState) {
        this.statusState = statusState;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        if (description != null) {
            return description;
        }
        StringBuilder sb = new StringBuilder();
        if (productName != null) {
            sb.append(productName);
        }
        if (categoryName != null) {
            sb.append(" • ").append(categoryName);
        }
        if (statusState != null) {
            sb.append(" • ").append(statusState);
        }
        return sb.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return productName;
    }
}
