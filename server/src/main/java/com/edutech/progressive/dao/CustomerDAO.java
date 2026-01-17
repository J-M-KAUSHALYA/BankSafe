package com.edutech.progressive.dao;


import java.util.List;

import com.edutech.progressive.dto.CustomerAccountInfo;
import com.edutech.progressive.entity.Customers;

public interface CustomerDAO {
    int addCustomer(Customers customers);
    Customers getCustomerById(int customerId);
    void updateCustomer(Customers customers);
    void deleteCustomer(int customerId);
    List<Customers> getAllCustomers();
    CustomerAccountInfo getCustomerAccountInfo(int customerId);
}