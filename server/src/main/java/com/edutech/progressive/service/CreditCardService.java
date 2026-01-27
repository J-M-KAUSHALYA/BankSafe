package com.edutech.progressive.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.progressive.entity.CreditCard;
import com.edutech.progressive.repository.CreditCardRepository;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    public CreditCard getCreditCardById(Long id) {
        return creditCardRepository.findById(id).orElse(null);
    }

    public CreditCard createCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    public void updateCreditCard(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }

    public void deleteCreditCard(Long id) {
        creditCardRepository.deleteById(id);
    }
}