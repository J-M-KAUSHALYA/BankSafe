package com.edutech.progressive.dao;
import com.edutech.progressive.dto.CustomerAccountInfo;
import com.edutech.progressive.entity.Customers;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {
    int addCustomer(Customers customers) throws SQLException;
    Customers getCustomerById(int customerId) throws SQLException;
    void updateCustomer(Customers customers) throws SQLException;
    void deleteCustomer(int customerId) throws SQLException;
    List<Customers> getAllCustomers() throws SQLException;
    CustomerAccountInfo getCustomerAccountInfo(int customerId) throws SQLException;
}