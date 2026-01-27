package com.edutech.progressive.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edutech.progressive.entity.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}