package com.marketplace.shop.model;

public class Permission {

    private int permissionId;
    private String permissionCode;
    private String statuseCodeFk;

    public Permission() {
    }

    public Permission(int permissionId, String permissionCode, String statuseCodeFk) {
        this.permissionId = permissionId;
        this.permissionCode = permissionCode;
        this.statuseCodeFk = statuseCodeFk;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getStatuseCodeFk() {
        return statuseCodeFk;
    }

    public void setStatuseCodeFk(String statuseCodeFk) {
        this.statuseCodeFk = statuseCodeFk;
    }

    @Override
    public String toString() {
        return permissionCode;
    }
}
