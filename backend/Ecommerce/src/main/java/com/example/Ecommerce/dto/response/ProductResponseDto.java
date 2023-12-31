package com.example.Ecommerce.dto.response;

import com.example.Ecommerce.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponseDto {
    String productName;
    String sellerName;
    int quantity;
    ProductStatus productStatus;
}
