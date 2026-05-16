package com.marketplace.shop.model;

public class Customer {

    private int customerId;
    private String customerName;
    private String customerEmail;
    private String phoneNo;
    private String statuseCodeFk;
    private String city;
    private String streetName;
    private String buildingNo;
    private String apartmentNo;

    public Customer() {
    }

    public Customer(int customerId, String customerName, String customerEmail, String phoneNo,
            String statuseCodeFk, String city, String streetName, String buildingNo, String apartmentNo) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.phoneNo = phoneNo;
        this.statuseCodeFk = statuseCodeFk;
        this.city = city;
        this.streetName = streetName;
        this.buildingNo = buildingNo;
        this.apartmentNo = apartmentNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStatuseCodeFk() {
        return statuseCodeFk;
    }

    public void setStatuseCodeFk(String statuseCodeFk) {
        this.statuseCodeFk = statuseCodeFk;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    @Override
    public String toString() {
        return customerName;
    }
}
