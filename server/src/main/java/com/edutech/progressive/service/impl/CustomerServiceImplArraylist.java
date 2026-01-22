package com.edutech.progressive.service.impl;
 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.service.CustomerService;
 
@Service
public class CustomerServiceImplArraylist implements CustomerService{
    private static List<Customers> customerList = new ArrayList<>();
 
    @Override
    public List<Customers> getAllCustomers() throws SQLException {
        return customerList;
    }
 
    @Override
    public int addCustomer(Customers customers) throws SQLException {
        customerList.add(customers);
        return customerList.size();
    }
 
    @Override
    public List<Customers> getAllCustomersSortedByName() throws SQLException {
        Collections.sort(customerList);
        return customerList;
    }
 
    public void emptyArrayList(){
        customerList.clear();
    }
}