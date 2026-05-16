package com.marketplace.shop.dao;

import com.marketplace.shop.model.Return;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO implements GenericDAO<Return> {

    private static final String SELECT_BASE =
        "SELECT r.*, p.product_name FROM returns r " +
        "JOIN products p ON r.product_code_fk = p.product_code ";

    @Override
    public void insert(Return r) throws SQLException {
        String sql = "INSERT INTO returns (product_code_fk, order_id_fk, refund, reason, date) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getProductCodeFk());
            ps.setInt(2, r.getOrderIdFk());
            ps.setBigDecimal(3, r.getRefund());
            ps.setString(4, r.getReason());
            ps.setDate(5, Date.valueOf(r.getDate()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) r.setReturnId(keys.getInt(1));
        }
    }

    @Override
    public void update(Return r) throws SQLException {
        String sql = "UPDATE returns SET product_code_fk=?, order_id_fk=?, refund=?, reason=?, date=? WHERE return_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, r.getProductCodeFk());
            ps.setInt(2, r.getOrderIdFk());
            ps.setBigDecimal(3, r.getRefund());
            ps.setString(4, r.getReason());
            ps.setDate(5, Date.valueOf(r.getDate()));
            ps.setInt(6, r.getReturnId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("DELETE FROM returns WHERE return_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Return findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement(SELECT_BASE + "WHERE r.return_id=?")) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Return> findAll() throws SQLException {
        List<Return> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + "ORDER BY r.return_id DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Return> findByOrder(int orderId) throws SQLException {
        List<Return> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement(SELECT_BASE + "WHERE r.order_id_fk=? ORDER BY r.return_id DESC")) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Return mapRow(ResultSet rs) throws SQLException {
        Return r = new Return();
        r.setReturnId(rs.getInt("return_id"));
        r.setProductCodeFk(rs.getString("product_code_fk"));
        r.setOrderIdFk(rs.getInt("order_id_fk"));
        r.setRefund(rs.getBigDecimal("refund"));
        r.setReason(rs.getString("reason"));
        r.setDate(rs.getDate("date").toLocalDate());
        r.setProductName(rs.getString("product_name"));
        return r;
    }
}
