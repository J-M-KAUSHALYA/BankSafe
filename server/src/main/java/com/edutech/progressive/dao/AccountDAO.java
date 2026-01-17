package com.edutech.progressive.dao;


import java.util.List;

import com.edutech.progressive.entity.Accounts;

public interface AccountDAO {
    int addAccount(Accounts accounts);
    Accounts getAccountById(int accountId);
    void updateAccount(Accounts accounts);
    void deleteAccount(int accountId);
    List<Accounts> getAllAccounts();
}