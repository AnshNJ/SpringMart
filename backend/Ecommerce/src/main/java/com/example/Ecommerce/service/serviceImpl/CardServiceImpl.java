package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.CardRequestDto;
import com.example.Ecommerce.dto.response.CardResponseDto;
import com.example.Ecommerce.enums.CardType;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.model.Card;
import com.example.Ecommerce.model.Customer;
import com.example.Ecommerce.repository.CardRepository;
import com.example.Ecommerce.repository.CustomerRepository;
import com.example.Ecommerce.service.CardService;
import com.example.Ecommerce.transformer.CardTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    CardRepository cardRepo;

    @Override
    public ResponseEntity addCard(CardRequestDto cardRequestDto) throws CustomerNotFound {
        //get customer
        Customer customer = customerRepo.findByMobNo(cardRequestDto.getMobNo());
        if(customer == null){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.FOUND);
        }

        Card card = CardTransformer.CardRequestDtoToCard(cardRequestDto);
        card.setCustomer(customer);
        customer.getCard().add(card);

        customerRepo.save(customer);

        CardResponseDto cardResponseDto = CardTransformer.CardToCardResponseDto(card);
        cardResponseDto.setCustomerName(customer.getName());
        return new ResponseEntity(cardResponseDto , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getAllVisaCards(int customerId) throws CustomerNotFound {
        List<Card> cardList = new ArrayList<>();
        try{
            cardList = customerRepo.findById(customerId).get().getCard();
        } catch(Exception e){
            return new ResponseEntity(new CustomerNotFound("Invalid Customer ID").getMessage() , HttpStatus.FOUND);
        }

        List<Card> visaCards = new ArrayList<>();
        for(Card card : cardList){
            if(card.getCardType() == CardType.VISA){
                visaCards.add(card);
            }
        }
        if(visaCards.size() == 0) return new ResponseEntity("No Cards Found" , HttpStatus.BAD_REQUEST);

        return new ResponseEntity(visaCards , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getAllMastercards(int customerId , LocalDate date) throws CustomerNotFound {
            List<Card> cardList;
            try{
                cardList = customerRepo.findById(customerId).get().getCard();
            } catch(Exception e){
                return new ResponseEntity(new CustomerNotFound("Invalid Customer ID").getMessage() , HttpStatus.FOUND);
            }

        List<Card> masterCards = new ArrayList<>();
        for(Card card : cardList){
            if(card.getCardType() == CardType.MASTERCARD && card.getExpiryDate().compareTo(date) > 0){
                masterCards.add(card);
            }
        }

        if(masterCards.size() == 0) return new ResponseEntity("No Cards Found" , HttpStatus.BAD_REQUEST);

        return new ResponseEntity(masterCards , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getMaxCard() {
        List<Card> cardList = cardRepo.findAll();
        HashMap<CardType , Integer> freq = new HashMap<>();
        int maxFreq = 0;
        String res = "";
        for(Card card : cardList){
            freq.put(card.getCardType() , freq.getOrDefault(card.getCardType() ,0) + 1);
            if(freq.get(card.getCardType()) > maxFreq){
                maxFreq = freq.get(card.getCardType());
                res = card.getCardType().toString();
            }
        }

        return new ResponseEntity(res+" is the most used card" , HttpStatus.FOUND);
    }
}
