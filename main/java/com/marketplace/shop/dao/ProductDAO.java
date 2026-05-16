package com.marketplace.shop.dao;

import com.marketplace.shop.model.Product;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements GenericDAO<Product> {

    private static final String SELECT_BASE =
            "SELECT p.*, c.category_name, ps.state, " +
            " (SELECT pi.image_url FROM product_images pi " +
            "    WHERE pi.product_code_fk = p.product_code " +
            "    ORDER BY pi.is_primary DESC, pi.image_id DESC LIMIT 1) AS image_url " +
            "FROM products p " +
            "JOIN categories c ON p.category_id_fk = c.category_id " +
            "JOIN products_statuse ps ON p.product_statuse_id_fk = ps.product_statuse_id ";

    @Override
    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (product_code, product_name, unit_price, quantity, category_id_fk, product_statuse_id_fk, discounts_code_fk) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getProductName());
            ps.setBigDecimal(3, p.getUnitPrice());
            ps.setInt(4, p.getQuantity());
            ps.setInt(5, p.getCategoryIdFk());
            ps.setInt(6, p.getProductStatuseIdFk());
            ps.setString(7, p.getDiscountsCodeFk());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET product_name=?, unit_price=?, quantity=?, category_id_fk=?, product_statuse_id_fk=?, discounts_code_fk=? WHERE product_code=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getProductName());
            ps.setBigDecimal(2, p.getUnitPrice());
            ps.setInt(3, p.getQuantity());
            ps.setInt(4, p.getCategoryIdFk());
            ps.setInt(5, p.getProductStatuseIdFk());
            ps.setString(6, p.getDiscountsCodeFk());
            ps.setString(7, p.getProductCode());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM products WHERE product_code=?")) {
            ps.setString(1, (String) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Product findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SELECT_BASE + "WHERE p.product_code=?")) {
            ps.setString(1, (String) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Product> findByCategory(int categoryId) throws SQLException {
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SELECT_BASE + "WHERE p.category_id_fk=?")) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Product> search(String keyword) throws SQLException {
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                SELECT_BASE + "WHERE p.product_name LIKE ? OR p.product_code LIKE ?")) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductCode(rs.getString("product_code"));
        p.setProductName(rs.getString("product_name"));
        p.setUnitPrice(rs.getBigDecimal("unit_price"));
        p.setQuantity(rs.getInt("quantity"));
        p.setCategoryIdFk(rs.getInt("category_id_fk"));
        p.setProductStatuseIdFk(rs.getInt("product_statuse_id_fk"));
        p.setDiscountsCodeFk(rs.getString("discounts_code_fk"));
        p.setCategoryName(rs.getString("category_name"));
        p.setStatusState(rs.getString("state"));
        try { p.setImageUrl(rs.getString("image_url")); } catch (SQLException ignore) {}
        return p;
    }
}
