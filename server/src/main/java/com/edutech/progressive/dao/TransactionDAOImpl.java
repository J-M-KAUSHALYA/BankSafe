package com.edutech.progressive.dao;

import java.util.List;

import com.edutech.progressive.entity.Transactions;

public class TransactionDAOImpl implements TransactionDAO{

    @Override
    public int addTransaction(Transactions transaction) {
        return -1;
    }

    @Override
    public Transactions getTransactionById(int transactionId) {
       return null;
    }

    @Override
    public void updateTransaction(Transactions transaction) {
        
        throw new UnsupportedOperationException("Unimplemented method 'updateTransaction'");
    }

    @Override
    public void deleteTransaction(int transactionId) {
       
    }

    @Override
    public List<Transactions> getAllTransactions() {
       return null;
    }

    



}
