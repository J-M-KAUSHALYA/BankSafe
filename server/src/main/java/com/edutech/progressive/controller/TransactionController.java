package com.edutech.progressive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.progressive.entity.Transactions;
import com.edutech.progressive.exception.AccountNotFoundException;
import com.edutech.progressive.exception.OutOfBalanceException;
import com.edutech.progressive.exception.WithdrawalLimitException;
import com.edutech.progressive.service.impl.AccountServiceImplJpa;
import com.edutech.progressive.service.impl.TransactionServiceImplJpa;

import java.sql.SQLException;
import java.util.List;

// public class TransactionController {

//     public ResponseEntity<List<Transactions>> getAllTransactions() {
//         return null;
//     }
//     public ResponseEntity<Transactions> getTransactionById(int transactionId) {
//         return null;
//     }
//     public ResponseEntity<Integer> addTransaction(Transactions transaction) {
//         return null;
//     }
//     public ResponseEntity<Void> updateTransaction(int transactionId, Transactions transaction) {
//         return null;
//     }
//     public ResponseEntity<Void> deleteTransaction(int transactionId) {
//         return null;
//     }
// }

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionServiceImplJpa transactionServiceImplJpa;

    @Autowired
    public TransactionController(TransactionServiceImplJpa transactionServiceImplJpa) {
        this.transactionServiceImplJpa = transactionServiceImplJpa;
    }

    @Autowired
    private AccountServiceImplJpa accountServiceImplJpa;

    @GetMapping
    public ResponseEntity<List<Transactions>> getAllTransactions() {
        try {
            List<Transactions> transactions = transactionServiceImplJpa.getAllTransactions();
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transactions> getTransactionById(@PathVariable int transactionId) {
        try {
            Transactions transaction = transactionServiceImplJpa.getTransactionById(transactionId);
            if (transaction != null) {
                return new ResponseEntity<>(transaction, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody Transactions transaction) {
        try {
            int transactionId = transactionServiceImplJpa.addTransaction(transaction);
            return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
        } catch (WithdrawalLimitException | OutOfBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            return new ResponseEntity<>("Unable to process your request at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(@PathVariable int transactionId, @RequestBody Transactions transaction) {
        try {
            transaction.setTransactionId(transactionId);
            transactionServiceImplJpa.updateTransaction(transaction);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int transactionId) {
        try {
            transactionServiceImplJpa.deleteTransaction(transactionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/{customerId}")
    public ResponseEntity<?> getAllTransactionsByCustomerId(@PathVariable int customerId) {
        try {

            List<Transactions> transactions = transactionServiceImplJpa.getTransactionsByCustomerId(customerId);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            return new ResponseEntity<>("Unable to process your request at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}