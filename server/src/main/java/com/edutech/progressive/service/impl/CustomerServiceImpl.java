package com.edutech.progressive.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.edutech.progressive.dao.CustomerDAO;
import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    CustomerDAO customerDAO;
    private static List<Customers> customersList = new ArrayList<>();

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public List<Customers> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }

    @Override
    public Customers getCustomerById(int customerId) throws SQLException {
        return customerDAO.getCustomerById(customerId);
    }

    @Override
    public int addCustomer(Customers customers) throws SQLException {
        return customerDAO.addCustomer(customers);
    }

    @Override
    public void updateCustomer(Customers customers) throws SQLException {
        customerDAO.updateCustomer(customers);
    }

    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        customerDAO.deleteCustomer(customerId);
    }

    @Override
    public List<Customers> getAllCustomersSortedByName() throws SQLException {
        List<Customers> sortedCustomers = customerDAO.getAllCustomers();
        if (sortedCustomers != null) {
            Collections.sort(sortedCustomers);
        }
        return sortedCustomers;
    }
}