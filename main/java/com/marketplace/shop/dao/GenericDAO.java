package com.marketplace.shop.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    void insert(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(Object id) throws SQLException;
    T findById(Object id) throws SQLException;
    List<T> findAll() throws SQLException;
}
