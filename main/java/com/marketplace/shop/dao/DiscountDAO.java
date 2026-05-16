package com.marketplace.shop.dao;

import com.marketplace.shop.model.Discount;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO implements GenericDAO<Discount> {

    @Override
    public void insert(Discount d) throws SQLException {
        String sql = "INSERT INTO discounts (discounts_code, total_discounts, total_in_percentage) VALUES (?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getDiscountsCode());
            ps.setBigDecimal(2, d.getTotalDiscounts());
            ps.setBigDecimal(3, d.getTotalInPercentage());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Discount d) throws SQLException {
        String sql = "UPDATE discounts SET total_discounts=?, total_in_percentage=? WHERE discounts_code=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setBigDecimal(1, d.getTotalDiscounts());
            ps.setBigDecimal(2, d.getTotalInPercentage());
            ps.setString(3, d.getDiscountsCode());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("DELETE FROM discounts WHERE discounts_code=?")) {
            ps.setString(1, (String) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Discount findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("SELECT * FROM discounts WHERE discounts_code=?")) {
            ps.setString(1, (String) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Discount> findAll() throws SQLException {
        List<Discount> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM discounts ORDER BY discounts_code")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Discount mapRow(ResultSet rs) throws SQLException {
        return new Discount(rs.getString("discounts_code"),
                            rs.getBigDecimal("total_discounts"),
                            rs.getBigDecimal("total_in_percentage"));
    }
}
