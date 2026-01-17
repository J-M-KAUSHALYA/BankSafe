package com.edutech.progressive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edutech.progressive.entity.Accounts;
import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.entity.Transactions;
import com.edutech.progressive.repository.AccountRepository;
import com.edutech.progressive.repository.CustomerRepository;
import com.edutech.progressive.repository.TransactionRepository;
import com.edutech.progressive.service.*;
import com.edutech.progressive.service.impl.AccountServiceImplJpa;
import com.edutech.progressive.service.impl.CustomerServiceImplJpa;
import com.edutech.progressive.service.impl.TransactionServiceImplJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankSafeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DayEightTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws SQLException {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.clearTestDatabase();
        MockitoAnnotations.openMocks(this);
    }

    private void clearTestDatabase() throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/mydb";
        final String user = "root";
        final String password = "root";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM transactions")) {
            statement.executeUpdate();
        }
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts")) {
            statement.executeUpdate();
        }
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM customers")) {
            statement.executeUpdate();
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

    Accounts getAccountsObject(Integer accId, Customers customers, double balance) {
        Accounts accounts = new Accounts();
        if (accId!=null) {
            accounts.setAccountId(accId.intValue());
        }
        setDynamicProperty(accounts, "customer", customers);
        accounts.setBalance(balance);
        return accounts;
    }

    Transactions getTransactionsObject(Integer id, Accounts accounts, String type, Date transDate, double amount) {
        Transactions transactions = new Transactions();
        if (id!=null) {
            transactions.setTransactionId(id.intValue());
        }
        setDynamicProperty(transactions, "accounts", accounts);
        transactions.setTransactionType(type);
        transactions.setTransactionDate(transDate);
        transactions.setAmount(amount);
        return transactions;
    }

    public void setDynamicProperty(Object entity, String propertyName, Object value) {
        try {
            Field field = entity.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            // Handle exception
        }
    }

        // Day - 8 Test cases

    @Test
    void testGetTransactionById_Day8() throws SQLException {
        AccountService accountService = new AccountServiceImplJpa(accountRepository);
        CustomerService customerService = new CustomerServiceImplJpa(customerRepository);
        TransactionService transactionService = new TransactionServiceImplJpa(transactionRepository, accountRepository);
        Customers customer1 = getCustomerObject(null,"John Doe", "john@example.com", "john");
        int id = customerService.addCustomer(customer1);
        customer1.setCustomerId(id);
        Accounts accounts1 = getAccountsObject(null, customer1, 5000);
        int accId = accountService.addAccount(accounts1);
        accounts1.setAccountId(accId);
        Transactions transactions = getTransactionsObject(null, accounts1, "Credit", new Date(), 500);
        int trancId = transactionService.addTransaction(transactions);

        Transactions response = transactionService.getTransactionById(trancId);

        assertEquals(response.getTransactionId(), trancId);
        assertEquals(500, response.getAmount());
    }

    @Test
    public void testGetTransactionsByIdController_Day8() throws Exception {
        AccountService accountService = new AccountServiceImplJpa(accountRepository);
        CustomerService customerService = new CustomerServiceImplJpa(customerRepository);
        TransactionService transactionService = new TransactionServiceImplJpa(transactionRepository, accountRepository);
        Customers customer1 = getCustomerObject(null,"John Doe", "john@example.com", "john");
        int id = customerService.addCustomer(customer1);
        customer1.setCustomerId(id);
        Accounts accounts1 = getAccountsObject(null, customer1, 5000);
        accountService.addAccount(accounts1);
        Transactions transactions1 = getTransactionsObject(null, accounts1, "Credit", new Date(), 500);
        int tranId = transactionService.addTransaction(transactions1);

        mockMvc.perform(get("/transactions/" + tranId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    public void testAddTransactionController_Day8() throws Exception {
        AccountService accountService = new AccountServiceImplJpa(accountRepository);
        CustomerService customerService = new CustomerServiceImplJpa(customerRepository);
        TransactionService transactionService = new TransactionServiceImplJpa(transactionRepository, accountRepository);
        Customers customer1 = getCustomerObject(null,"John Doe", "john@example.com", "john");
        int id = customerService.addCustomer(customer1);
        customer1.setCustomerId(id);
        Accounts accounts1 = getAccountsObject(null, customer1, 5000);
        accountService.addAccount(accounts1);
        Transactions transactions1 = getTransactionsObject(null, accounts1, "Credit", new Date(), 500);

        mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactions1)))
                .andExpect(status().isCreated());
    }
}
