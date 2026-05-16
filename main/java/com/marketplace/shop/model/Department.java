package com.marketplace.shop.model;

public class Department {

    private int departmentId;
    private String departmentName;
    private String statuseCodeFk;
    private String city;
    private String streetName;
    private String buildingNo;
    private String apartmentNo;

    public Department() {
    }

    public Department(int departmentId, String departmentName, String statuseCodeFk,
            String city, String streetName, String buildingNo, String apartmentNo) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.statuseCodeFk = statuseCodeFk;
        this.city = city;
        this.streetName = streetName;
        this.buildingNo = buildingNo;
        this.apartmentNo = apartmentNo;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
        return departmentName;
    }
}
