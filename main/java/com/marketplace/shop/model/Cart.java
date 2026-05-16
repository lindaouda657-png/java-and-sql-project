package com.marketplace.shop.model;

import java.time.LocalDateTime;

public class Cart {

    private int cartId;
    private int customerIdFk;
    private LocalDateTime createdAt;

    public Cart() {
    }

    public Cart(int cartId, int customerIdFk) {
        this.cartId = cartId;
        this.customerIdFk = customerIdFk;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCustomerIdFk() {
        return customerIdFk;
    }

    public void setCustomerIdFk(int customerIdFk) {
        this.customerIdFk = customerIdFk;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
