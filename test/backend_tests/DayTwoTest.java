package com.edutech.progressive;
import com.edutech.progressive.config.DatabaseConnectionManager;
import com.edutech.progressive.dao.*;
import com.edutech.progressive.entity.Accounts;
import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.entity.Transactions;
import com.edutech.progressive.service.*;
import com.edutech.progressive.service.impl.AccountServiceImpl;
import com.edutech.progressive.service.impl.AccountServiceImplArraylist;
import com.edutech.progressive.service.impl.CustomerServiceImpl;
import com.edutech.progressive.service.impl.CustomerServiceImplArraylist;
import com.edutech.progressive.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayTwoTest {
  private CustomerServiceImpl customerService;
  private CustomerServiceImplArraylist customerServiceImplArray;
  private AccountServiceImpl accountService;
  private AccountServiceImplArraylist accountServiceImplArray;

  @BeforeEach
  public void setUp() {
      String url = "jdbc:mysql://localhost:3306/mydb";
      String user = "root";
      String password = "root";
      try (Connection connection = DriverManager.getConnection(url, user, password);
           Statement statement = connection.createStatement()) {
          List<String> tableNames = List.of("customers", "accounts", "transactions");
          for (String tableName:tableNames) {
              String deleteQuery = "DELETE FROM " + tableName;
              statement.executeUpdate(deleteQuery);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  // Helper classes to create Objects
  Customers getCustomerObject(Integer id, String name, String email, String username) {
      Customers customers = new Customers();
      if (id!=null) {
          customers.setCustomerId(id.intValue());
      }
      customers.setName(name);
      customers.setEmail(email);
      customers.setUsername(username);
      customers.setPassword("password");
      return customers;
  }

  Accounts getAccountsObject(Integer accId, Integer custId, double balance) {
      Accounts accounts = new Accounts();
      accounts.setAccountId(accId);
      accounts.setCustomerId(custId);
      accounts.setBalance(balance);
      return accounts;
  }


   @Test
   public void testAddCustomerToArrayList_Day2() throws SQLException {
       customerServiceImplArray = new CustomerServiceImplArraylist();
       customerServiceImplArray.emptyArrayList();
       Customers customer = getCustomerObject(1, "John Doe", "john@example.com", "john");
       int result = customerServiceImplArray.addCustomer(customer);
       assertNotNull(result);
       assertEquals(result, 1);
   }

   @Test
   public void testGetAllCustomersSortedByName_Day2() throws SQLException {
       customerServiceImplArray = new CustomerServiceImplArraylist();
       customerServiceImplArray.emptyArrayList();
       Customers customer1 = getCustomerObject(1, "John Doe", "john@example.com", "john");
       Customers customer2 = getCustomerObject(2, "Alice Smith", "alice@example.com", "alice");
       Customers customer3 = getCustomerObject(3, "Bob Johnson", "bob@example.com", "bob");

       customerServiceImplArray.addCustomer(customer2);
       customerServiceImplArray.addCustomer(customer1);
       customerServiceImplArray.addCustomer(customer3);

       List<Customers> result = customerServiceImplArray.getAllCustomersSortedByName();
       assertNotNull(result);
       assertFalse(result.isEmpty());

       assertTrue(result.get(0).getName().compareTo(result.get(1).getName()) <= 0);
       assertTrue(result.get(1).getName().compareTo(result.get(2).getName()) <= 0);
   }

   @Test
   public void testGetCustomerById_Day2() throws SQLException {
       customerServiceImplArray = new CustomerServiceImplArraylist();
       customerServiceImplArray.emptyArrayList();
       Customers customer = getCustomerObject(1, "John Doe", "john@example.com", "john");
       int result = customerServiceImplArray.addCustomer(customer);
       assertNotNull(result);
       assertEquals(1, result);
   }

   @Test
   public void testGetAllAccountsSortedByName_Day2() throws SQLException {
       accountServiceImplArray = new AccountServiceImplArraylist();
       accountServiceImplArray.emptyArrayList();
       Accounts account1 = getAccountsObject(1,1,200.0);
       Accounts account2 = getAccountsObject(2,2,300.0);
       Accounts account3 = getAccountsObject(3,3,500.0);

       accountServiceImplArray.addAccount(account1);
       accountServiceImplArray.addAccount(account2);
       accountServiceImplArray.addAccount(account3);
       List<Accounts> result = accountServiceImplArray.getAllAccountsSortedByBalance();

       assertNotNull(result);
       assertFalse(result.isEmpty());

       // Check if the list is sorted by name
       assertTrue(result.get(0).getBalance() == account1.getBalance());
       assertTrue(result.get(1).getBalance() <= account2.getBalance());
   }

   @Test
   public void testAccountToArrayList_Day2() throws SQLException {
       accountServiceImplArray = new AccountServiceImplArraylist();
       accountServiceImplArray.emptyArrayList();
       Accounts account1 = getAccountsObject(1,1,200.0);
       int result = accountServiceImplArray.addAccount(account1);
       assertNotNull(result);
       assertEquals(1, result);
   }

   @Test
   public void testGetAccountById_Day2() throws SQLException {
       accountServiceImplArray = new AccountServiceImplArraylist();
       accountServiceImplArray.emptyArrayList();
       Accounts account1 = getAccountsObject(1,1,200.0);
       int result = accountServiceImplArray.addAccount(account1);
       assertNotNull(result);
       assertEquals(1, result);
   }

   @Test
   public void testGetAccountByIdNonExistent_Day2() throws SQLException {
       accountServiceImplArray = new AccountServiceImplArraylist();
       Accounts result = accountServiceImplArray.getAccountById(99);
       assertNull(result);
   }

   @Test
   public void testGetCustomerByIdNonExistent_Day2() throws SQLException {
       customerServiceImplArray = new CustomerServiceImplArraylist();
       Customers result = customerServiceImplArray.getCustomerById(99);
       assertNull(result);
   }

}
