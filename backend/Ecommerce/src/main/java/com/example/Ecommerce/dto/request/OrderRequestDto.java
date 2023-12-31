package com.example.Ecommerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderRequestDto {
    //We'll require customer ID as well as the card no. that the customer wishes to use
    int customerId;

    String cardNo;

    int cvv;
}
