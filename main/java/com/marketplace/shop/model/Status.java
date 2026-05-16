package com.marketplace.shop.model;

public class Status {

    private String statuseCode;
    private String statuseName;

    public Status() {
    }

    public Status(String statuseCode, String statuseName) {
        this.statuseCode = statuseCode;
        this.statuseName = statuseName;
    }

    public String getStatuseCode() {
        return statuseCode;
    }

    public void setStatuseCode(String statuseCode) {
        this.statuseCode = statuseCode;
    }

    public String getStatuseName() {
        return statuseName;
    }

    public void setStatuseName(String statuseName) {
        this.statuseName = statuseName;
    }

    @Override
    public String toString() {
        return statuseName;
    }
}
