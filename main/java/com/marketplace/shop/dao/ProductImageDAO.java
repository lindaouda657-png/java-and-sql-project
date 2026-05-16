package com.marketplace.shop.dao;

import com.marketplace.shop.model.ProductImage;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO {

    public void insert(ProductImage img) throws SQLException {
        String sql = "INSERT INTO product_images (product_code_fk, image_url, is_primary) VALUES (?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, img.getProductCodeFk());
            ps.setString(2, img.getImageUrl());
            ps.setInt(3, img.isPrimary() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    public void clearPrimary(String productCode) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("UPDATE product_images SET is_primary=0 WHERE product_code_fk=?")) {
            ps.setString(1, productCode);
            ps.executeUpdate();
        }
    }

    public String findPrimaryUrl(String productCode) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                "SELECT image_url FROM product_images WHERE product_code_fk=? ORDER BY is_primary DESC, image_id DESC LIMIT 1")) {
            ps.setString(1, productCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
        }
        return null;
    }

    public List<ProductImage> findByProduct(String productCode) throws SQLException {
        List<ProductImage> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("SELECT * FROM product_images WHERE product_code_fk=? ORDER BY is_primary DESC")) {
            ps.setString(1, productCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductImage pi = new ProductImage();
                pi.setImageId(rs.getInt("image_id"));
                pi.setProductCodeFk(rs.getString("product_code_fk"));
                pi.setImageUrl(rs.getString("image_url"));
                pi.setPrimary(rs.getInt("is_primary") == 1);
                list.add(pi);
            }
        }
        return list;
    }
}
