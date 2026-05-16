package com.marketplace.shop.model;

import java.math.BigDecimal;

public class Discount {

    private String discountsCode;
    private BigDecimal totalDiscounts;
    private BigDecimal totalInPercentage;

    public Discount() {
    }

    public Discount(String discountsCode, BigDecimal totalDiscounts, BigDecimal totalInPercentage) {
        this.discountsCode = discountsCode;
        this.totalDiscounts = totalDiscounts;
        this.totalInPercentage = totalInPercentage;
    }

    public String getDiscountsCode() {
        return discountsCode;
    }

    public void setDiscountsCode(String discountsCode) {
        this.discountsCode = discountsCode;
    }

    public BigDecimal getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(BigDecimal totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public BigDecimal getTotalInPercentage() {
        return totalInPercentage;
    }

    public void setTotalInPercentage(BigDecimal totalInPercentage) {
        this.totalInPercentage = totalInPercentage;
    }

    @Override
    public String toString() {
        return discountsCode + " (" + totalInPercentage + "%)";
    }
}
