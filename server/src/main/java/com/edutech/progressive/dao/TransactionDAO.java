package com.edutech.progressive.dao;

import java.sql.SQLException;
import java.util.List;

import com.edutech.progressive.entity.Transactions;

public interface TransactionDAO {
    int addTransaction(Transactions transaction);
    Transactions getTransactionById(int transactionId);
    void updateTransaction(Transactions transaction);
    void deleteTransaction(int transactionId) ;
    List<Transactions> getAllTransactions();
}
