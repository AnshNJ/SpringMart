package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.ItemRequestDto;
import com.example.Ecommerce.dto.request.OrderRequestDto;
import org.springframework.http.ResponseEntity;

public interface CartService {
    public ResponseEntity addCart(ItemRequestDto itemRequestDto);

    public ResponseEntity checkoutCart(OrderRequestDto orderRequestDto) throws Exception;

    public ResponseEntity deleteFromCart(int customerId, int itemId) throws Exception;

    public ResponseEntity viewEntireCart(int customerId);
}
