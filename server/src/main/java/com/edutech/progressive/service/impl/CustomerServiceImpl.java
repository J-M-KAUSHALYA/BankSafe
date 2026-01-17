package com.edutech.progressive.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.edutech.progressive.dao.CustomerDAO;
import com.edutech.progressive.dao.CustomerDAOImpl;
import com.edutech.progressive.entity.Customers;

public class CustomerServiceImpl {

    private CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAOImpl customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customers> getAllCustomers() throws SQLException{
        return null;
    }

    public int addCustomer(Customers customers) throws SQLException{
        return -1;
    }

    public List<Customers> getAllCustomersSortedByName() throws SQLException{
        return null;
    }

    public void updateCustomer(Customers customers) throws SQLException{

    }

    public void deleteCustomer(int customerId) throws SQLException{

    }

    public Customers getCustomerById(int customerId) throws SQLException{
        return null;
    }


}