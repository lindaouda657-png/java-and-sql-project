package com.marketplace.shop.dao;

import com.marketplace.shop.model.User;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements GenericDAO<User> {

    @Override
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (user_name, user_email, statuse_code_fk, department_id_fk, city, street_name, building_NO, apartment_NO) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setString(3, user.getStatuseCodeFk());
            ps.setInt(4, user.getDepartmentIdFk());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getStreetName());
            ps.setString(7, user.getBuildingNo());
            ps.setString(8, user.getApartmentNo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) user.setUserId(keys.getInt(1));
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET user_name=?, user_email=?, statuse_code_fk=?, department_id_fk=?, city=?, street_name=?, building_NO=?, apartment_NO=? WHERE user_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setString(3, user.getStatuseCodeFk());
            ps.setInt(4, user.getDepartmentIdFk());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getStreetName());
            ps.setString(7, user.getBuildingNo());
            ps.setString(8, user.getApartmentNo());
            ps.setInt(9, user.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM users WHERE user_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public User findById(Object id) throws SQLException {
        String sql = "SELECT u.*, d.department_name, s.statuse_name FROM users u " +
                     "JOIN department d ON u.department_id_fk = d.department_id " +
                     "JOIN statuses s ON u.statuse_code_fk = s.statuse_code WHERE u.user_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.*, d.department_name, s.statuse_name FROM users u " +
                     "JOIN department d ON u.department_id_fk = d.department_id " +
                     "JOIN statuses s ON u.statuse_code_fk = s.statuse_code";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT u.*, d.department_name, s.statuse_name FROM users u " +
                     "JOIN department d ON u.department_id_fk = d.department_id " +
                     "JOIN statuses s ON u.statuse_code_fk = s.statuse_code WHERE u.user_email=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUserName(rs.getString("user_name"));
        u.setUserEmail(rs.getString("user_email"));
        u.setStatuseCodeFk(rs.getString("statuse_code_fk"));
        u.setDepartmentIdFk(rs.getInt("department_id_fk"));
        u.setCity(rs.getString("city"));
        u.setStreetName(rs.getString("street_name"));
        u.setBuildingNo(rs.getString("building_NO"));
        u.setApartmentNo(rs.getString("apartment_NO"));
        u.setDepartmentName(rs.getString("department_name"));
        u.setStatusName(rs.getString("statuse_name"));
        try { u.setUserSalary(rs.getBigDecimal("user_salary")); } catch (SQLException ignore) {}
        try { u.setPhoneNo(rs.getString("phone_NO")); } catch (SQLException ignore) {}
        return u;
    }
}
