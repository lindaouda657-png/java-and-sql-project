package com.marketplace.shop.model;

public class DepartmentPermission {
    private int departmentPerId;
    private int departmentIdFk;
    private int permissionIdFk;
    private String statuseIdFk;

    public DepartmentPermission() {}
    public DepartmentPermission(int departmentPerId, int departmentIdFk, int permissionIdFk, String statuseIdFk) {
        this.departmentPerId = departmentPerId;
        this.departmentIdFk = departmentIdFk;
        this.permissionIdFk = permissionIdFk;
        this.statuseIdFk = statuseIdFk;
    }

    public int getDepartmentPerId() { return departmentPerId; }
    public void setDepartmentPerId(int departmentPerId) { this.departmentPerId = departmentPerId; }
    public int getDepartmentIdFk() { return departmentIdFk; }
    public void setDepartmentIdFk(int departmentIdFk) { this.departmentIdFk = departmentIdFk; }
    public int getPermissionIdFk() { return permissionIdFk; }
    public void setPermissionIdFk(int permissionIdFk) { this.permissionIdFk = permissionIdFk; }
    public String getStatuseIdFk() { return statuseIdFk; }
    public void setStatuseIdFk(String statuseIdFk) { this.statuseIdFk = statuseIdFk; }
}
