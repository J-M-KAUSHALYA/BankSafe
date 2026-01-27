package com.edutech.progressive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.progressive.entity.Loan;
import com.edutech.progressive.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan l = loanService.getLoanById(id);
        if (l==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(l);
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoan(loan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        Loan l = loanService.getLoanById(id);
        // if (l==null) {
        //     return ResponseEntity.notFound().build();
        // }
        loan.setId(id);
        loanService.updateLoan(loan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        Loan l = loanService.getLoanById(id);
        // if (l==null) {
        //     return ResponseEntity.notFound().build();
        // }
        loanService.deleteLoan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}