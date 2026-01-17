package com.edutech.progressive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edutech.progressive.BankSafeApplication;
import com.edutech.progressive.entity.*;
import com.edutech.progressive.repository.AccountRepository;
import com.edutech.progressive.repository.CustomerRepository;
import com.edutech.progressive.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankSafeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DayTwelveThirteenTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws SQLException {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        objectMapper = new ObjectMapper();
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
        accounts.setCustomer(customers);
        accounts.setBalance(balance);
        return accounts;
    }

    Transactions getTransactionsObject(Integer id, Accounts accounts, String type, Date transDate, double amount) {
        Transactions transactions = new Transactions();
        if (id!=null) {
            transactions.setTransactionId(id.intValue());
        }
        transactions.setAccounts(accounts);
        transactions.setTransactionType(type);
        transactions.setTransactionDate(transDate);
        transactions.setAmount(amount);
        return transactions;
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void testCustomerControllerGetAllCustomers_Day13() throws Exception {
        customerRepository.deleteAll();
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customerRepository.save(customer);
        Customers customer2 = getCustomerObject(null,"Alice Smith", "alice@example.com", "alice");
        customerRepository.save(customer2);

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void testCustomerControllerAddCustomer_Day13() throws Exception {
        customerRepository.deleteAll();
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer.setRole("USER");
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());

        // Verify that the customer was added to the database
        List<Customers> customers = customerRepository.findAll();
        assertEquals(1, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void testCustomerControllerDeleteCustomer_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customerRepository.save(customer);

        mockMvc.perform(delete("/customers/{customerId}", customer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(customerRepository.existsById(customer.getCustomerId()));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void testAccountControllerAddAccount_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer = customerRepository.save(customer);
        Accounts accounts1 = getAccountsObject(null, customer, 5000);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accounts1)))
                .andExpect(status().isCreated());

        List<Accounts> accounts = accountRepository.findAll();
        assertEquals(1, accounts.size());
        assertEquals(customer.getCustomerId(), accounts.get(0).getCustomer().getCustomerId());
        assertEquals(5000, accounts.get(0).getBalance(), 0.01);
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    public void testAccountControllerGetAccountById_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer = customerRepository.save(customer);
        Accounts accounts1 = getAccountsObject(null, customer, 5000);
        accountRepository.save(accounts1);
        Accounts accounts2 = getAccountsObject(null, customer, 2599);
        accountRepository.save(accounts2);

        mockMvc.perform(get("/accounts/{accountId}", accounts1.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customer.customerId", is(customer.getCustomerId())))
                .andExpect(jsonPath("$.balance", is(5000.0)));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void testAccountControllerDeleteAccount_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer = customerRepository.save(customer);
        Accounts accounts = getAccountsObject(null, customer, 5000);
        accountRepository.save(accounts);

        mockMvc.perform(delete("/accounts/{accountId}", accounts.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(accountRepository.existsById(accounts.getAccountId()));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    public void testTransactionControllerAddTransaction_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer = customerRepository.save(customer);
        Accounts accounts = getAccountsObject(null, customer, 5000);
        accountRepository.save(accounts);
        Transactions transactions = getTransactionsObject(null, accounts, "credit", new Date(), 500);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactions)))
                .andExpect(status().isCreated());


        List<Transactions> transactionsList = transactionRepository.findAll();
        assertEquals(1, transactionsList.size());
        assertEquals(accounts.getAccountId(), transactionsList.get(0).getAccounts().getAccountId());
        assertEquals(500.0, transactionsList.get(0).getAmount(), 0.01);
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    public void testTransactionControllerDeleteTransaction_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer = customerRepository.save(customer);
        Accounts accounts = getAccountsObject(null, customer, 5000);
        accountRepository.save(accounts);
        Transactions transaction = getTransactionsObject(null, accounts, "credit", new Date(), 500);
        transaction = transactionRepository.save(transaction);

        mockMvc.perform(delete("/transactions/{transactionId}", transaction.getTransactionId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(transactionRepository.existsById(transaction.getTransactionId()));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUnauthorizedAddCustomerByUser_Day13() throws Exception {
        customerRepository.deleteAll();
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer.setRole("USER");
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testUnauthorizedDeleteAccountByUser_Day13() throws Exception {
        Customers customer = getCustomerObject(null,"John Doe", "john@example.com", "john");
        customer = customerRepository.save(customer);
        Accounts accounts = getAccountsObject(null, customer, 5000);
        accountRepository.save(accounts);

        mockMvc.perform(delete("/accounts/{accountId}", accounts.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isForbidden());
    }
}
