package com.marketplace.shop.model;

public class Review {

    private String reviewCode;
    private String theTopic;
    private int rate;
    private String productCodeFkU;
    private int customerIdFkU;

    private String productName;
    private String customerName;

    public Review() {
    }

    public Review(String reviewCode, String theTopic, int rate, String productCodeFkU, int customerIdFkU) {
        this.reviewCode = reviewCode;
        this.theTopic = theTopic;
        this.rate = rate;
        this.productCodeFkU = productCodeFkU;
        this.customerIdFkU = customerIdFkU;
    }

    public String getReviewCode() {
        return reviewCode;
    }

    public void setReviewCode(String reviewCode) {
        this.reviewCode = reviewCode;
    }

    public String getTheTopic() {
        return theTopic;
    }

    public void setTheTopic(String theTopic) {
        this.theTopic = theTopic;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getProductCodeFkU() {
        return productCodeFkU;
    }

    public void setProductCodeFkU(String productCodeFkU) {
        this.productCodeFkU = productCodeFkU;
    }

    public int getCustomerIdFkU() {
        return customerIdFkU;
    }

    public void setCustomerIdFkU(int customerIdFkU) {
        this.customerIdFkU = customerIdFkU;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
