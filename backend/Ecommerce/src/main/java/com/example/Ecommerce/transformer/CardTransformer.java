package com.example.Ecommerce.transformer;

import com.example.Ecommerce.dto.request.CardRequestDto;
import com.example.Ecommerce.dto.response.CardResponseDto;
import com.example.Ecommerce.model.Card;

public class CardTransformer {
    public static CardResponseDto CardToCardResponseDto(Card card){
        return CardResponseDto.builder()
                .cardNo(card.getCardNo())
                .build();
    }

    public static Card CardRequestDtoToCard(CardRequestDto cardRequestDto){
        return Card.builder()
                .cardNo(cardRequestDto.getCardNo())
                .cardType(cardRequestDto.getCardType())
                .cvv(cardRequestDto.getCvv())
                .expiryDate(cardRequestDto.getExpiryDate())
                .build();
    }
}
