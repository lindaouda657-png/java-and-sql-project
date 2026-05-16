package com.marketplace.shop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItem {

    private int cartItemId;
    private int cartIdFk;
    private String productCodeFk;
    private int quantity;
    private LocalDateTime addedAt;
    private String productName;
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public CartItem(int cartItemId, int cartIdFk, String productCodeFk, int quantity) {
        this.cartItemId = cartItemId;
        this.cartIdFk = cartIdFk;
        this.productCodeFk = productCodeFk;
        this.quantity = quantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartIdFk() {
        return cartIdFk;
    }

    public void setCartIdFk(int cartIdFk) {
        this.cartIdFk = cartIdFk;
    }

    public String getProductCodeFk() {
        return productCodeFk;
    }

    public void setProductCodeFk(String productCodeFk) {
        this.productCodeFk = productCodeFk;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
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

    public BigDecimal getSubtotal() {
        if (unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
