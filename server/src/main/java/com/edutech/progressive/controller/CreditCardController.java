package com.edutech.progressive.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.progressive.entity.CreditCard;
import com.edutech.progressive.service.CreditCardService;

import java.util.List;
@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        return ResponseEntity.ok(creditCardService.getAllCreditCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCreditCardById(@PathVariable Long id) {
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        if (creditCard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(creditCard);
    }

    @PostMapping
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody CreditCard creditCard) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creditCardService.createCreditCard(creditCard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCreditCard(@PathVariable Long id, @RequestBody CreditCard creditCard) {
        CreditCard cc = creditCardService.getCreditCardById(id);
        if (cc == null) {
            return ResponseEntity.notFound().build();
        }
        creditCard.setId(id);
        creditCardService.updateCreditCard(creditCard);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        CreditCard existingCreditCard = creditCardService.getCreditCardById(id);
        if (existingCreditCard != null) {
            creditCardService.deleteCreditCard(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}