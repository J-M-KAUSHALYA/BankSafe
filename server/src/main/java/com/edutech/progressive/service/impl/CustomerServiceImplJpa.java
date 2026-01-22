package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.exception.CustomerAlreadyExistsException;
import com.edutech.progressive.repository.AccountRepository;
import com.edutech.progressive.repository.CustomerRepository;
import com.edutech.progressive.repository.TransactionRepository;
import com.edutech.progressive.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceImplJpa implements CustomerService {

    // PasswordEncoder passwordEncoder;
    // TransactionRepository transactionRepository;
    // AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImplJpa( CustomerRepository customerRepository) {
        // this.passwordEncoder = passwordEncoder;
        // this.transactionRepository = transactionRepository;
        // this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customers> getAllCustomers() throws SQLException {
        return customerRepository.findAll();
    }

    

    @Override
    public Customers getCustomerById(int customerId) throws SQLException {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    public int addCustomer(Customers customers) throws SQLException {
        // Customers existingCustomer = customerRepository.findByEmail(customers.getEmail());
        // if (existingCustomer != null) {
        //     throw new CustomerAlreadyExistsException(
        //             "Customer with give email already exists : " + customers.getEmail());
        // }
        // Customers oldUser = customerRepository.findByUsername(customers.getUsername());
        // if (oldUser != null) {
        //     throw new CustomerAlreadyExistsException(
        //             "Customer with give username already exists : " + customers.getUsername());
        // }
        // customers.setPassword(passwordEncoder.encode(customers.getPassword()));
        // return customerRepository.save(customers).getCustomerId();

        // return -1;
        return customerRepository.save(customers).getCustomerId();
    }

    @Override
    public void updateCustomer(Customers customers) throws SQLException {
        // Customers existingCustomer =
        // customerRepository.findByEmail(customers.getEmail());
        // if (existingCustomer != null && customers.getCustomerId() !=
        // existingCustomer.getCustomerId()) {
        // throw new CustomerAlreadyExistsException("Customer with give email already exists : " + customers.getEmail());
        // }
        // Customers oldUser =
        // customerRepository.findByUsername(customers.getUsername());
        // if (oldUser != null && customers.getCustomerId() != oldUser.getCustomerId())
        // {
        // throw new CustomerAlreadyExistsException("Customer with give username already exists : " + customers.getUsername());
        // }
        // if (!customers.getRole().isBlank()) {
        // if (!existingCustomer.getPassword().equals(customers.getPassword())) {
        // customers.setPassword(passwordEncoder.encode(customers.getPassword()));
        // }
        // customerRepository.save(customers);
        // } else {
        // throw new SQLException("Role for a customer cannot be empty");
        // }
        customerRepository.save(customers);
    }

    @Override
    @Transactional
    public void deleteCustomer(int customerId) throws SQLException {
        // transactionRepository.deleteByCustomerId(customerId);
        // accountRepository.deleteByCustomerId(customerId);
        customerRepository.deleteByCustomerId(customerId);
    }

    @Override
    public List<Customers> getAllCustomersSortedByName() throws SQLException {
        List<Customers> sortedCustomers = customerRepository.findAll();
        Collections.sort(sortedCustomers);
        return sortedCustomers;

    }
}