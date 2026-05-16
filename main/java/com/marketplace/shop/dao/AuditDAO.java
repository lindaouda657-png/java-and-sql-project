package com.marketplace.shop.dao;

import com.marketplace.shop.model.Audit;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditDAO implements GenericDAO<Audit> {

    private static final String SELECT_BASE =
        "SELECT a.*, u.user_name AS employee_name, p.product_name FROM audits a " +
        "JOIN users u ON a.user_id_fk = u.user_id " +
        "JOIN products p ON a.product_code_fk = p.product_code ";

    @Override
    public void insert(Audit a) throws SQLException {
        String sql = "INSERT INTO audits (audit_code, action_type, old_state, new_state, user_id_fk, product_code_fk) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getAuditCode());
            ps.setString(2, a.getActionType());
            ps.setString(3, a.getOldState());
            ps.setString(4, a.getNewState());
            ps.setInt(5, a.getUserIdFk());
            ps.setString(6, a.getProductCodeFk());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Audit a) throws SQLException {  }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("DELETE FROM audits WHERE audit_code=?")) {
            ps.setString(1, (String) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Audit findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement(SELECT_BASE + "WHERE a.audit_code=?")) {
            ps.setString(1, (String) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Audit> findAll() throws SQLException {
        List<Audit> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + "ORDER BY a.date DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Audit mapRow(ResultSet rs) throws SQLException {
        Audit a = new Audit();
        a.setAuditCode(rs.getString("audit_code"));
        a.setActionType(rs.getString("action_type"));
        a.setOldState(rs.getString("old_state"));
        a.setNewState(rs.getString("new_state"));
        Timestamp ts = rs.getTimestamp("date");
        if (ts != null) a.setDate(ts.toLocalDateTime());
        a.setUserIdFk(rs.getInt("user_id_fk"));
        a.setProductCodeFk(rs.getString("product_code_fk"));
        a.setEmployeeName(rs.getString("employee_name"));
        a.setProductName(rs.getString("product_name"));
        return a;
    }
}
