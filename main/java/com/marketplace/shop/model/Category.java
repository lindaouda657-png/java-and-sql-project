package com.marketplace.shop.model;

public class Category {

    private int categoryId;
    private String categoryName;
    private String statuseCodeFk;

    public Category() {
    }

    public Category(int categoryId, String categoryName, String statuseCodeFk) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.statuseCodeFk = statuseCodeFk;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatuseCodeFk() {
        return statuseCodeFk;
    }

    public void setStatuseCodeFk(String statuseCodeFk) {
        this.statuseCodeFk = statuseCodeFk;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
