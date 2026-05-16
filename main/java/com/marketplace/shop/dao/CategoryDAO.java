package com.marketplace.shop.dao;

import com.marketplace.shop.model.Category;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements GenericDAO<Category> {

    @Override
    public void insert(Category c) throws SQLException {
        String sql = "INSERT INTO categories (category_name, statuse_code_fk) VALUES (?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getStatuseCodeFk());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setCategoryId(keys.getInt(1));
        }
    }

    @Override
    public void update(Category c) throws SQLException {
        String sql = "UPDATE categories SET category_name=?, statuse_code_fk=? WHERE category_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getStatuseCodeFk());
            ps.setInt(3, c.getCategoryId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM categories WHERE category_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Category findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM categories WHERE category_id=?")) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM categories")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setCategoryName(rs.getString("category_name"));
        c.setStatuseCodeFk(rs.getString("statuse_code_fk"));
        return c;
    }
}
