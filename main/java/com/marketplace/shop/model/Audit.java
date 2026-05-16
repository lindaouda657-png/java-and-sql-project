package com.marketplace.shop.model;

import java.time.LocalDateTime;

public class Audit {

    private String auditCode;
    private String actionType;
    private String oldState;
    private String newState;
    private LocalDateTime date;
    private int userIdFk;
    private String productCodeFk;

    private String employeeName;
    private String productName;

    public Audit() {
    }

    public Audit(String auditCode, String actionType, String oldState, String newState,
            LocalDateTime date, int userIdFk, String productCodeFk) {
        this.auditCode = auditCode;
        this.actionType = actionType;
        this.oldState = oldState;
        this.newState = newState;
        this.date = date;
        this.userIdFk = userIdFk;
        this.productCodeFk = productCodeFk;
    }

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getOldState() {
        return oldState;
    }

    public void setOldState(String oldState) {
        this.oldState = oldState;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(int userIdFk) {
        this.userIdFk = userIdFk;
    }

    public String getProductCodeFk() {
        return productCodeFk;
    }

    public void setProductCodeFk(String productCodeFk) {
        this.productCodeFk = productCodeFk;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
