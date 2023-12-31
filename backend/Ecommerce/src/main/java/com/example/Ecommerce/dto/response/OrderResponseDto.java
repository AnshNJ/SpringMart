package com.example.Ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {
    String orderNo;

    int totalValue;

    LocalDate orderDate;

    String cardUsed;

    List<ItemResponseDto> itemList;

    String customerName;
}
