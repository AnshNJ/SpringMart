package com.example.Ecommerce.dto.response;

import com.example.Ecommerce.model.Item;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartResponseDto {
    Integer cartTotal;

    Integer numberOfItems;

    String customerName;

    List<ItemResponseDto> itemList = new ArrayList<>();
}
