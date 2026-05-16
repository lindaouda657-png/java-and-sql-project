package com.marketplace.shop.model;

import java.time.LocalDateTime;

public class UserCredential {

    private int credentialId;
    private int userIdFk;
    private String passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserCredential() {
    }

    public UserCredential(int credentialId, int userIdFk, String passwordHash) {
        this.credentialId = credentialId;
        this.userIdFk = userIdFk;
        this.passwordHash = passwordHash;
    }

    public int getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(int credentialId) {
        this.credentialId = credentialId;
    }

    public int getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(int userIdFk) {
        this.userIdFk = userIdFk;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
