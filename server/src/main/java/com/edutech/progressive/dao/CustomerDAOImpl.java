package com.edutech.progressive.dao;

import java.util.List;

import com.edutech.progressive.dto.CustomerAccountInfo;
import com.edutech.progressive.entity.Customers;

public class CustomerDAOImpl implements CustomerDAO{

    @Override
    public int addCustomer(Customers customers) {
        return -1;
    }

    @Override
    public Customers getCustomerById(int customerId) {
        return null;
    }

    @Override
    public void updateCustomer(Customers customers) {
        
    }

    @Override
    public void deleteCustomer(int customerId) {
        
    }

    @Override
    public List<Customers> getAllCustomers() {
       return null;
    }

    @Override
    public CustomerAccountInfo getCustomerAccountInfo(int customerId) {
        return null;
    }

}
