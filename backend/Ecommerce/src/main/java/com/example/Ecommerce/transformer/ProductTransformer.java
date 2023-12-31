package com.example.Ecommerce.transformer;

import com.example.Ecommerce.dto.request.ProductRequestDto;
import com.example.Ecommerce.dto.response.ProductResponseDto;
import com.example.Ecommerce.enums.ProductStatus;
import com.example.Ecommerce.model.Product;
import lombok.Data;

public class ProductTransformer {
    public static Product ProductRequestDtoToProduct(ProductRequestDto productRequestDto){

        return Product.builder()
                .productName(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .productCategory(productRequestDto.getProductCategory())
                .quantity(productRequestDto.getQuantity())
                .productStatus(ProductStatus.AVAILABLE)
                .build();
    }

    public static ProductResponseDto ProductToProductResponseDto(Product product){
        return ProductResponseDto.builder()
                .productName(product.getProductName())
                .sellerName(product.getSeller().getName())
                .quantity(product.getQuantity())
                .productStatus(product.getProductStatus())
                .build();
    }
}
