package com.edutech.progressive.dao;


import java.sql.SQLException;
import java.util.List;

import com.edutech.progressive.entity.Accounts;

public interface AccountDAO {
    int addAccount(Accounts accounts) throws SQLException;
    Accounts getAccountById(int accountId) throws SQLException;
    void updateAccount(Accounts accounts) throws SQLException;
    void deleteAccount(int accountId) throws SQLException;
    List<Accounts> getAllAccounts() throws SQLException;
}