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

public class DayThreeTest {
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


   private void insertSampleCustomers() throws SQLException {
       insertSampleCustomer("John Doe", "john@example.com", "johnDoe", "password");
       insertSampleCustomer("Jane Smith", "jane@example.com", "janemsith", "password");
   }
   private int insertSampleCustomer(String name, String email, String username, String password) throws SQLException {
       try (Connection connection = DatabaseConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO customers (name, email, username, password) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
           statement.setString(1, name);
           statement.setString(2, email);
           statement.setString(3, username);
           statement.setString(4, password);
           statement.executeUpdate();

           int generatedID = -1;
           try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                   generatedID = generatedKeys.getInt(1);
               }
           }
           return generatedID;
       }
   }

   @Test
   public void testGetAllCustomers_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       // Insert sample customers into the test database
       insertSampleCustomers();

       List<Customers> customers = customerService.getAllCustomers();

       assertNotNull(customers);
       assertEquals(2, customers.size()); // Adjust as per your test data
   }


   @Test
   public void testAddCustomer_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());

       Customers newCustomers = getCustomerObject(null, "Jane Smith", "jane@example.com","jhon");

       int id = customerService.addCustomer(newCustomers);

       Customers customers = customerService.getCustomerById(id);
       assertNotNull(customers);
       assertEquals(newCustomers.getCustomerId(), customers.getCustomerId());
       assertEquals(newCustomers.getName(), customers.getName());
       assertEquals(newCustomers.getEmail(), customers.getEmail());
   }

   @Test
   public void testUpdateCustomer_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());

       // Insert a sample customer into the test database
       int customerId = insertSampleCustomer("Alice Johnson", "alice@example.com", "alicejohn", "password");

       Customers updatedCustomers = getCustomerObject(customerId, "Updated Name", "updated@example.com","jhon");

       customerService.updateCustomer(updatedCustomers);

       Customers retrievedCustomers = customerService.getCustomerById(customerId);

       assertNotNull(retrievedCustomers);
       assertEquals(updatedCustomers.getName(), retrievedCustomers.getName());
       assertEquals(updatedCustomers.getEmail(), retrievedCustomers.getEmail());
   }

   @Test
   public void testDeleteCustomer_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       // Insert a sample customer into the test database
       int customerId = insertSampleCustomer("Bob Brown", "bob@example.com", "bobbrown", "password");
       Customers savedCustomer = customerService.getCustomerById(customerId);
       assertNotNull(savedCustomer);

       customerService.deleteCustomer(customerId);

       Customers deletedCustomers = customerService.getCustomerById(customerId);

       assertNull(deletedCustomers);
   }

   @Test
   public void testGetAllCustomersSortedByName_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       // Insert sample customers into the test database
       insertSampleCustomers();

       List<Customers> sortedCustomers = customerService.getAllCustomersSortedByName();

       assertNotNull(sortedCustomers);
       assertEquals(2, sortedCustomers.size()); // Adjust as per your test data
       assertTrue(sortedCustomers.get(0).getName().compareTo(sortedCustomers.get(1).getName()) < 0);
   }

   @Test
   public void testAddAccount_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());

       Customers customers = getCustomerObject(0, "John Doe", "john.example.com","jhon");
       int customerId = customerService.addCustomer(customers);
       Accounts accountsToAdd = new Accounts(0, customerId, 1000.0);
       int generatedAccountId = accountService.addAccount(accountsToAdd);

       Accounts retrievedAccounts = accountService.getAccountById(generatedAccountId);

       assertNotNull(retrievedAccounts);
       assertEquals(generatedAccountId, retrievedAccounts.getAccountId());
       assertEquals(1000.0, retrievedAccounts.getBalance());
   }

   @Test
   public void testUpdateAccount_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());

       Customers customers = getCustomerObject(0, "John Doe", "john.example.com","jhon");
       int customerId = customerService.addCustomer(customers);
       Accounts accountsToAdd = new Accounts();
       accountsToAdd.setCustomerId(customerId);
       accountsToAdd.setBalance(1000);

       int generatedAccountId = accountService.addAccount(accountsToAdd);

       Accounts retrievedAccounts = accountService.getAccountById(generatedAccountId);

       retrievedAccounts.setBalance(1500.0);
       accountService.updateAccount(retrievedAccounts);

       Accounts updatedAccounts = accountService.getAccountById(generatedAccountId);

       assertNotNull(updatedAccounts);
       assertEquals(generatedAccountId, updatedAccounts.getAccountId());
       assertEquals(1500.0, updatedAccounts.getBalance());
   }

   @Test
   public void testDeleteAccount_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());

       Customers customers = getCustomerObject(0, "John Doe", "john.example.com","jhon");
       int customerId = customerService.addCustomer(customers);
       assertNotEquals(-1, customerId);

       Accounts accountsToAdd = getAccountsObject(0, customerId, 1000.0);
       int generatedAccountId = accountService.addAccount(accountsToAdd);

       accountService.deleteAccount(generatedAccountId);

       assertNull(accountService.getAccountById(generatedAccountId));
   }

   @Test
   public void testGetAllAccounts_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());

       Customers customers = getCustomerObject(0, "John Doe", "john.example.com","jhon");
       int customerId = customerService.addCustomer(customers);

       Accounts accounts1 = getAccountsObject(0, customerId, 1000.0);
       Accounts accounts2 = getAccountsObject(0, customerId, 2000.0);
       Accounts accounts3 = getAccountsObject(0, customerId, 3000.0);
       accountService.addAccount(accounts1);
       accountService.addAccount(accounts2);
       accountService.addAccount(accounts3);

       List<Accounts> allAccounts = accountService.getAllAccounts();

       assertNotNull(allAccounts);
       assertTrue(allAccounts.size() == 3);
   }

   @Test
   public void testGetAllAccountsSortedByBalance_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());

       Customers customers = getCustomerObject(0, "John Doe", "john.example.com","joe");
       int customerId = customerService.addCustomer(customers);
       List<Accounts> unsortedAccounts = new ArrayList<>();
       unsortedAccounts.add(getAccountsObject(1, customerId, 500.0));
       unsortedAccounts.add(getAccountsObject(2, customerId, 200.0));
       unsortedAccounts.add(getAccountsObject(3, customerId, 700.0));

       for (Accounts accounts : unsortedAccounts) {
           accountService.addAccount(accounts);
       }

       List<Accounts> sortedAccounts = accountService.getAllAccountsSortedByBalance();

       assertNotNull(sortedAccounts);
       assertEquals(sortedAccounts.get(0).getBalance(), 200.0);
       assertEquals(sortedAccounts.get(1).getBalance(), 500.0);
       assertEquals(sortedAccounts.get(2).getBalance(), 700.0);
   }

   @Test
   public void testGetAllTransactions_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());
       TransactionService transactionService = new TransactionServiceImpl(new TransactionDAOImpl());

       Customers customers = getCustomerObject(0, "John Doe", "john.example.com","jhon");
       int customerId = customerService.addCustomer(customers);

       Accounts accounts1 = new Accounts(0, customerId, 1000.0);
       int accountId = accountService.addAccount(accounts1);

       List<Transactions> transactions = new ArrayList<>();
       transactions.add(new Transactions(1, accountId, 100.0, new Date(), "1"));
       transactions.add(new Transactions(2, accountId, 200.0, new Date(), "2"));
       transactions.add(new Transactions(3, accountId, 50.0, new Date(), "1"));

       // Insert the transactions into the database (or mock)
       for (Transactions transaction : transactions) {
           transactionService.addTransaction(transaction);
       }

       // Call the method to get all transactions
       List<Transactions> retrievedTransactions = transactionService.getAllTransactions();
       assertNotNull(retrievedTransactions);
       // Assert that the retrieved transactions match the expected transactions
       assertEquals(retrievedTransactions.size(), 3);
   }

   @Test
   public void testGetTransactionById_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());
       TransactionService transactionService = new TransactionServiceImpl(new TransactionDAOImpl());

       Customers customers = new Customers(0, "John Doe", "john.example.com","jhon","abcd1234");
       int customerId = customerService.addCustomer(customers);

       Accounts accounts1 = new Accounts(0, customerId, 1000.0);
       int accountId = accountService.addAccount(accounts1);

       Transactions transaction = new Transactions(1, accountId, 100.0, new Date(), "1");
       int transactionId = transactionService.addTransaction(transaction);

       // Call the method to get the transaction by ID
       Transactions retrievedTransaction = transactionService.getTransactionById(transactionId);

       assertNotNull(retrievedTransaction);
       // Assert that the retrieved transaction matches the expected transaction
       assertEquals(transaction.getAccountId(), retrievedTransaction.getAccountId());
       assertEquals(transaction.getAmount(), retrievedTransaction.getAmount());
   }

   @Test
   public void testUpdateTransaction_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());
       TransactionService transactionService = new TransactionServiceImpl(new TransactionDAOImpl());

       Customers customers = new Customers(0, "John Doe", "john.example.com","jhon","abcd1234");
       int customerId = customerService.addCustomer(customers);
       Accounts accounts1 = new Accounts(0, customerId, 1000.0);
       int accountId = accountService.addAccount(accounts1);
       Transactions transaction = new Transactions(1, accountId, 100.0, new Date(), "1");

       // Insert the transaction into the database (or mock)
       int transactionId = transactionService.addTransaction(transaction);

       // Modify the transaction
       transaction.setAmount(200.0);

       // Update the transaction
       transactionService.updateTransaction(transaction);

       // Retrieve the updated transaction
       Transactions updatedTransaction = transactionService.getTransactionById(transactionId);
       assertNotNull(updatedTransaction);
       // Assert that the updated transaction matches the modified transaction
       assertEquals(transaction.getAmount(), updatedTransaction.getAmount());
   }

   @Test
   public void testDeleteTransaction_Day3() throws SQLException {
       customerService = new CustomerServiceImpl(new CustomerDAOImpl());
       accountService = new AccountServiceImpl(new AccountDAOImpl());
       TransactionService transactionService = new TransactionServiceImpl(new TransactionDAOImpl());

       Customers customers = new Customers(0, "John Doe", "john.example.com","jhon","abcd1234");
       int customerId = customerService.addCustomer(customers);
       Accounts accounts1 = new Accounts(0, customerId, 1000.0);
       int accountId = accountService.addAccount(accounts1);

       Transactions transaction = new Transactions(1, accountId, 100.0, new Date(), "1");

       // Insert the transaction into the database (or mock)
       int transactionId = transactionService.addTransaction(transaction);
       assertNotNull(transactionId);
       assertNotEquals(-1, transactionId);

       transactionService.deleteTransaction(transactionId);

       assertNull(transactionService.getTransactionById(transactionId));
   }
}
