package com.marketplace.shop.model;

import java.time.LocalDateTime;

public class Notification {

    private int notificationId;
    private Integer userIdFk;
    private Integer customerIdFk;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(int notificationId, Integer userIdFk, Integer customerIdFk, String message, boolean isRead) {
        this.notificationId = notificationId;
        this.userIdFk = userIdFk;
        this.customerIdFk = customerIdFk;
        this.message = message;
        this.isRead = isRead;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Integer userIdFk) {
        this.userIdFk = userIdFk;
    }

    public Integer getCustomerIdFk() {
        return customerIdFk;
    }

    public void setCustomerIdFk(Integer customerIdFk) {
        this.customerIdFk = customerIdFk;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
