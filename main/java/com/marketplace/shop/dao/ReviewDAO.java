package com.marketplace.shop.dao;

import com.marketplace.shop.model.Review;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private static final String BASE_SQL =
            "SELECT r.*, p.product_name, c.customer_name FROM reviews r " +
            "JOIN products p ON r.product_code_fk_u = p.product_code " +
            "JOIN customers c ON r.customer_id_fk_u = c.customer_id ";

    public List<Review> findAll() throws SQLException {
        List<Review> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(BASE_SQL + "ORDER BY r.review_code DESC")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Review> findByProduct(String productCode) throws SQLException {
        List<Review> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement(BASE_SQL + "WHERE r.product_code_fk_u=? ORDER BY r.review_code DESC")) {
            ps.setString(1, productCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void insert(Review r) throws SQLException {
        String sql = "INSERT INTO reviews (review_code, the_topic, rate, product_code_fk_u, customer_id_fk_u) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, r.getReviewCode());
            ps.setString(2, r.getTheTopic());
            ps.setInt(3, r.getRate());
            ps.setString(4, r.getProductCodeFkU());
            ps.setInt(5, r.getCustomerIdFkU());
            ps.executeUpdate();
        }
    }

    private Review map(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setReviewCode(rs.getString("review_code"));
        r.setTheTopic(rs.getString("the_topic"));
        r.setRate(rs.getInt("rate"));
        r.setProductCodeFkU(rs.getString("product_code_fk_u"));
        r.setCustomerIdFkU(rs.getInt("customer_id_fk_u"));
        r.setProductName(rs.getString("product_name"));
        r.setCustomerName(rs.getString("customer_name"));
        return r;
    }
}
