package com.marketplace.shop.dao;

import com.marketplace.shop.model.Customer;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements GenericDAO<Customer> {

    @Override
    public void insert(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (customer_name, customer_email, phone_NO, statuse_code_fk, city, street_name, building_NO, apartment_NO) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getCustomerEmail());
            ps.setString(3, c.getPhoneNo());
            ps.setString(4, c.getStatuseCodeFk() == null ? "ACTIVE" : c.getStatuseCodeFk());
            ps.setString(5, c.getCity());
            ps.setString(6, c.getStreetName());
            ps.setString(7, c.getBuildingNo());
            ps.setString(8, c.getApartmentNo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setCustomerId(keys.getInt(1));
        }
    }

    @Override
    public void update(Customer c) throws SQLException {
        String sql = "UPDATE customers SET customer_name=?, customer_email=?, phone_NO=?, statuse_code_fk=?, city=?, street_name=?, building_NO=?, apartment_NO=? WHERE customer_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getCustomerEmail());
            ps.setString(3, c.getPhoneNo());
            ps.setString(4, c.getStatuseCodeFk());
            ps.setString(5, c.getCity());
            ps.setString(6, c.getStreetName());
            ps.setString(7, c.getBuildingNo());
            ps.setString(8, c.getApartmentNo());
            ps.setInt(9, c.getCustomerId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        
        try (PreparedStatement psCartItems = conn.prepareStatement(
                "DELETE ci FROM cart_items ci JOIN cart c ON ci.cart_id_fk = c.cart_id WHERE c.customer_id_fk = ?")) {
            psCartItems.setInt(1, (Integer) id);
            psCartItems.executeUpdate();
        }
        try (PreparedStatement psCart = conn.prepareStatement(
                "DELETE FROM cart WHERE customer_id_fk = ?")) {
            psCart.setInt(1, (Integer) id);
            psCart.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM customers WHERE customer_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Customer findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM customers WHERE customer_id=?")) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customers")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Customer> search(String keyword) throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE customer_name LIKE ? OR customer_email LIKE ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Customer findByEmail(String email) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                "SELECT * FROM customers WHERE customer_email=?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public void setStatus(int customerId, String statusCode) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                "UPDATE customers SET statuse_code_fk=? WHERE customer_id=?")) {
            ps.setString(1, statusCode);
            ps.setInt(2, customerId);
            ps.executeUpdate();
        }
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setCustomerName(rs.getString("customer_name"));
        c.setCustomerEmail(rs.getString("customer_email"));
        c.setPhoneNo(rs.getString("phone_NO"));
        c.setStatuseCodeFk(rs.getString("statuse_code_fk"));
        c.setCity(rs.getString("city"));
        c.setStreetName(rs.getString("street_name"));
        c.setBuildingNo(rs.getString("building_NO"));
        c.setApartmentNo(rs.getString("apartment_NO"));
        return c;
    }
}
