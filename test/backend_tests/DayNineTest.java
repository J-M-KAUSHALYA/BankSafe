package com.edutech.progressive;

import com.edutech.progressive.BankSafeApplication;
import com.edutech.progressive.entity.Accounts;
import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.entity.Transactions;
import com.edutech.progressive.exception.AccountNotFoundException;
import com.edutech.progressive.exception.OutOfBalanceException;
import com.edutech.progressive.exception.WithdrawalLimitException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankSafeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DayNineTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
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
        customers.setPassword("root");
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
    public void testTranscationOutBalanceException_Day9() throws SQLException
    {
        AccountService accountService = new AccountServiceImplJpa(accountRepository);
        CustomerService customerService = new CustomerServiceImplJpa(customerRepository);
        TransactionService transactionService = new TransactionServiceImplJpa(transactionRepository, accountRepository);
        Customers customer1 = getCustomerObject(null,"John Doe", "john@example.com", "john");
        int id = customerService.addCustomer(customer1);
        customer1.setCustomerId(id);
        Accounts accounts1 = getAccountsObject(null, customer1, 5000);
        accountService.addAccount(accounts1);
        Transactions transaction = getTransactionsObject(null, accounts1, "debit", new Date(), 6000);

        assertThrows(OutOfBalanceException.class, () -> {
            transactionService.addTransaction(transaction);
        });
    }

    @Test
    public void testWithdrawalLimitException_Day9() throws SQLException
    {
        AccountService accountService = new AccountServiceImplJpa(accountRepository);
        CustomerService customerService = new CustomerServiceImplJpa(customerRepository);
        TransactionService transactionService = new TransactionServiceImplJpa(transactionRepository, accountRepository);
        Customers customer1 = getCustomerObject(null,"John Doe", "john@example.com", "john");
        int id = customerService.addCustomer(customer1);
        customer1.setCustomerId(id);
        Accounts accounts1 = getAccountsObject(null, customer1, 50000);
        accountService.addAccount(accounts1);
        Transactions transaction = getTransactionsObject(null, accounts1, "debit", new Date(), 31000);

        assertThrows(WithdrawalLimitException.class, () -> {
            transactionService.addTransaction(transaction);
        });
    }

    @Test
    public void testAccountNotFoundException_Day9() throws SQLException
    {
        CustomerService customerService = new CustomerServiceImplJpa(customerRepository);
        TransactionService transactionService = new TransactionServiceImplJpa(transactionRepository, accountRepository);
        Customers customer1 = getCustomerObject(null,"John Doe", "john@example.com", "john");
        int id = customerService.addCustomer(customer1);
        customer1.setCustomerId(id);

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.getTransactionsByCustomerId(id);
        });
    }
}
