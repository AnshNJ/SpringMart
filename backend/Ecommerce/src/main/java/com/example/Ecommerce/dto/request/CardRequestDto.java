package com.example.Ecommerce.dto.request;

import com.example.Ecommerce.enums.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardRequestDto {
    String mobNo;
    String cardNo;
    Integer cvv;
    LocalDate expiryDate;
    CardType cardType;
}
