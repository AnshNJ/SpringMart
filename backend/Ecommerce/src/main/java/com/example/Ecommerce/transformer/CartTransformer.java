package com.example.Ecommerce.transformer;

import com.example.Ecommerce.dto.response.CartResponseDto;
import com.example.Ecommerce.model.Cart;

public class CartTransformer {
    public static CartResponseDto CartToCartResponseDto(Cart cart){
        return CartResponseDto.builder()
                .cartTotal(cart.getCartTotal())
                .numberOfItems(cart.getNumberOfItems())
                .build();
    }
}
