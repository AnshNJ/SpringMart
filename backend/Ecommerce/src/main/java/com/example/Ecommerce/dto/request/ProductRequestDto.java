package com.example.Ecommerce.dto.request;

import com.example.Ecommerce.enums.ProductCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
public class ProductRequestDto {
    int sellerId;

    String productName;

    int price;

    int quantity;

    ProductCategory productCategory;
}