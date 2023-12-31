package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.CardRequestDto;
import com.example.Ecommerce.exception.CustomerNotFound;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface CardService {
    public ResponseEntity addCard(CardRequestDto cardRequestDto) throws CustomerNotFound;

    public ResponseEntity getAllVisaCards(int customerId) throws CustomerNotFound;

    public ResponseEntity getAllMastercards(int customerId , LocalDate date) throws CustomerNotFound;

    public ResponseEntity getMaxCard();
}
