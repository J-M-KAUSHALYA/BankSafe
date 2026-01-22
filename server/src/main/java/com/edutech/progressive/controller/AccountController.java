package com.edutech.progressive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.progressive.entity.Accounts;
import com.edutech.progressive.exception.AccountNotFoundException;
import com.edutech.progressive.service.AccountService;
import com.edutech.progressive.service.impl.AccountServiceImplJpa;

import java.sql.SQLException;
import java.util.List;

// public class AccountController {

//     public ResponseEntity<List<Accounts>> getAllAccounts() {
//         return null;
//     }

//     public ResponseEntity<Accounts> getAccountById(int accountId) {
//         return null;
//     }

//     public ResponseEntity<List<Accounts>> getAccountsByUser(String param) {
//         return null;
//     }

//     public ResponseEntity<Integer> addAccount(Accounts accounts) {
//         return null;
//     }

//     public ResponseEntity<Void> updateAccount(int accountId, Accounts accounts) {
//         return null;
//     }

//     public ResponseEntity<Void> deleteAccount(int accountId) {
//         return null;
//     }
// }

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountServiceImplJpa accountServiceImplJpa;

    @Autowired
    public AccountController(AccountServiceImplJpa accountServiceImplJpa) {
        this.accountServiceImplJpa = accountServiceImplJpa;
    }

    @GetMapping
    public ResponseEntity<List<Accounts>> getAllAccounts() {
        try {
            List<Accounts> accounts = accountServiceImplJpa.getAllAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable int accountId) {
        try {
            Accounts accounts = accountServiceImplJpa.getAccountById(accountId);
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to process your request at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Accounts>> getAccountsByUser(@PathVariable String userId) {
        try {
            List<Accounts> accounts = accountServiceImplJpa.getAccountsByUser(Integer.parseInt(userId));
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Integer> addAccount(@RequestBody Accounts accounts) {
        try {
            int accountId = accountServiceImplJpa.addAccount(accounts);
            return new ResponseEntity<>(accountId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Void> updateAccount(@PathVariable int accountId, @RequestBody Accounts accounts) {
        try {
            accounts.setAccountId(accountId);
            accountServiceImplJpa.updateAccount(accounts);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable int accountId) {
        try {
            accountServiceImplJpa.deleteAccount(accountId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}