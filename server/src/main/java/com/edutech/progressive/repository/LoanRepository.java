package com.edutech.progressive.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edutech.progressive.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{
}