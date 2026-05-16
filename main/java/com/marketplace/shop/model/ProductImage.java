package com.marketplace.shop.model;

import java.time.LocalDateTime;

public class ProductImage {

    private int imageId;
    private String productCodeFk;
    private String imageUrl;
    private boolean isPrimary;
    private LocalDateTime uploadedAt;

    public ProductImage() {
    }

    public ProductImage(int imageId, String productCodeFk, String imageUrl, boolean isPrimary) {
        this.imageId = imageId;
        this.productCodeFk = productCodeFk;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getProductCodeFk() {
        return productCodeFk;
    }

    public void setProductCodeFk(String productCodeFk) {
        this.productCodeFk = productCodeFk;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
