package com.edutech.progressive.service.impl;
import com.edutech.progressive.entity.Accounts;
import com.edutech.progressive.entity.Transactions;
import com.edutech.progressive.exception.AccountNotFoundException;
import com.edutech.progressive.exception.OutOfBalanceException;
import com.edutech.progressive.exception.WithdrawalLimitException;
import com.edutech.progressive.repository.AccountRepository;
import com.edutech.progressive.repository.TransactionRepository;
import com.edutech.progressive.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImplJpa implements TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImplJpa(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    
    @Override
    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
        // return null;

    }

    

    @Override
    public Transactions getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
        // return null;
        
    }

    @Override
    public int addTransaction(Transactions transaction) throws SQLException {
        Accounts account = accountRepository.findByAccountId(transaction.getAccounts().getAccountId());
        double balance = account.getBalance();
        if (transaction.getAmount() > 30000 && transaction.getTransactionType().equalsIgnoreCase("debit")) {
            throw new WithdrawalLimitException("Exceeded the withdrawal limit.");
        }
        if (balance < transaction.getAmount() && transaction.getTransactionType().equalsIgnoreCase("debit")) {
            throw new OutOfBalanceException("Transaction amount exceeds the total balance available in the account.");
        }
        if (transaction.getTransactionType().equalsIgnoreCase("credit")) {
            balance = balance + transaction.getAmount();
        }
        else {
            balance = balance - transaction.getAmount();
        }
        account.setBalance(balance);
        accountRepository.save(account);
        return transactionRepository.save(transaction).getTransactionId();
        // return -1;
        
    }

    @Override
    public void updateTransaction(Transactions transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(int transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @Override
    public List<Transactions> getTransactionsByCustomerId(int customerId) throws SQLException {
        List<Accounts> accountsList = accountRepository.getAccountsByCustomerCustomerId(customerId);
        List<Transactions> allTransactions = new ArrayList<>();
        if (accountsList.isEmpty()) {
            throw new AccountNotFoundException("No accounts found linked with this customer");
        }
        for (Accounts account : accountsList) {
            List<Transactions> transactionsForAccount = transactionRepository.findByAccountsAccountId(account.getAccountId());
            allTransactions.addAll(transactionsForAccount);
        }
        return allTransactions;
        // return null;
        
    }
}