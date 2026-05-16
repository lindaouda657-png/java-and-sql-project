package com.marketplace.shop.model;

public class User {

    private int userId;
    private String userName;
    private String userEmail;
    private String statuseCodeFk;
    private int departmentIdFk;
    private String city;
    private String streetName;
    private String buildingNo;
    private String apartmentNo;

    private String departmentName;
    private String statusName;
    private java.math.BigDecimal userSalary;
    private String phoneNo;

    public User() {
    }

    public User(int userId, String userName, String userEmail, String statuseCodeFk,
            int departmentIdFk, String city, String streetName, String buildingNo, String apartmentNo) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.statuseCodeFk = statuseCodeFk;
        this.departmentIdFk = departmentIdFk;
        this.city = city;
        this.streetName = streetName;
        this.buildingNo = buildingNo;
        this.apartmentNo = apartmentNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatuseCodeFk() {
        return statuseCodeFk;
    }

    public void setStatuseCodeFk(String statuseCodeFk) {
        this.statuseCodeFk = statuseCodeFk;
    }

    public int getDepartmentIdFk() {
        return departmentIdFk;
    }

    public void setDepartmentIdFk(int departmentIdFk) {
        this.departmentIdFk = departmentIdFk;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public java.math.BigDecimal getUserSalary() {
        return userSalary;
    }

    public void setUserSalary(java.math.BigDecimal userSalary) {
        this.userSalary = userSalary;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return userName;
    }
}
