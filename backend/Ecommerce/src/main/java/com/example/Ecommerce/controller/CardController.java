package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.request.CardRequestDto;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardService cardService;

    //ADD CARD
    @PostMapping("/add-card")
    public ResponseEntity addCard(@RequestBody CardRequestDto cardRequestDto) throws CustomerNotFound {
        return cardService.addCard(cardRequestDto);
    }

    //GET ALL VISA CARDS
    @GetMapping("/get-all-visa")
    public ResponseEntity getAllVisaCards(@RequestParam int customerId) throws CustomerNotFound {
        return cardService.getAllVisaCards(customerId);
    }

    //GET ALL MASTERCARD WITH EXPIRY GREATER THAN GIVEN DATE
    @GetMapping("/get-all-mastercard/{customerId}/{date}")
    public ResponseEntity getAllMastercards(@PathVariable int customerId , @PathVariable LocalDate date) throws CustomerNotFound {
        return cardService.getAllMastercards(customerId , date);
    }

    //GET CARD TYPE WITH MAX CARDS
    @GetMapping("/get-max-card")
    public ResponseEntity getMaxCard(){
        return cardService.getMaxCard();
    }

}
