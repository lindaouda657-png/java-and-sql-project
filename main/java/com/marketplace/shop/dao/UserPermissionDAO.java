package com.marketplace.shop.dao;

import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserPermissionDAO {

    public Set<String> findCodesForUser(int userId) throws SQLException {
        Set<String> codes = new HashSet<>();
        String sql = "SELECT p.permission_code " +
                     "FROM user_permissions up " +
                     "JOIN permissions p ON up.permission_id_fk = p.permission_id " +
                     "WHERE up.user_id_fk=? AND p.statuse_code_fk='ACTIVE'";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) codes.add(rs.getString(1));
        }
        return codes;
    }

    public Set<Integer> findPermissionIdsForUser(int userId) throws SQLException {
        Set<Integer> ids = new HashSet<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                "SELECT permission_id_fk FROM user_permissions WHERE user_id_fk=?")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getInt(1));
        }
        return ids;
    }

    public void replaceForUser(int userId, Set<Integer> permissionIds) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        boolean prevAuto = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            try (PreparedStatement del = con.prepareStatement(
                    "DELETE FROM user_permissions WHERE user_id_fk=?")) {
                del.setInt(1, userId);
                del.executeUpdate();
            }
            if (permissionIds != null && !permissionIds.isEmpty()) {
                try (PreparedStatement ins = con.prepareStatement(
                        "INSERT INTO user_permissions (user_id_fk, permission_id_fk) VALUES (?,?)")) {
                    for (Integer pid : permissionIds) {
                        ins.setInt(1, userId);
                        ins.setInt(2, pid);
                        ins.addBatch();
                    }
                    ins.executeBatch();
                }
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(prevAuto);
        }
    }
}
