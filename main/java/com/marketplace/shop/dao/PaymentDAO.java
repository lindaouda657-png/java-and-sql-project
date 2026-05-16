package com.marketplace.shop.dao;

import com.marketplace.shop.model.Payment;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements GenericDAO<Payment> {

    @Override
    public void insert(Payment p) throws SQLException {
        String sql = "INSERT INTO payments (order_id_fk, total, payment_method, customer_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getOrderIdFk());
            ps.setBigDecimal(2, p.getTotal());
            ps.setString(3, p.getPaymentMethod());
            ps.setInt(4, p.getCustomerId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setPaymentId(keys.getInt(1));
        }
    }

    @Override
    public void update(Payment p) throws SQLException {
        String sql = "UPDATE payments SET order_id_fk=?, total=?, payment_method=?, customer_id=? WHERE payment_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, p.getOrderIdFk());
            ps.setBigDecimal(2, p.getTotal());
            ps.setString(3, p.getPaymentMethod());
            ps.setInt(4, p.getCustomerId());
            ps.setInt(5, p.getPaymentId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM payments WHERE payment_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Payment findById(Object id) throws SQLException {
        String sql = "SELECT p.*, c.customer_name FROM payments p " +
                     "LEFT JOIN customers c ON p.customer_id = c.customer_id " +
                     "WHERE p.payment_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Payment> findAll() throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, c.customer_name FROM payments p " +
                     "LEFT JOIN customers c ON p.customer_id = c.customer_id";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setOrderIdFk(rs.getInt("order_id_fk"));
        p.setTotal(rs.getBigDecimal("total"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setCustomerId(rs.getInt("customer_id"));
        p.setEmployeeName(rs.getString("customer_name"));
        return p;
    }
}
