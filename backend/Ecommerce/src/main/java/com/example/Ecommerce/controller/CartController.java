package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.request.ItemRequestDto;
import com.example.Ecommerce.dto.request.OrderRequestDto;
import com.example.Ecommerce.dto.response.CartResponseDto;
import com.example.Ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    //ADD CART
    @PostMapping("/add-cart")
    public ResponseEntity addCart(@RequestBody ItemRequestDto itemRequestDto){
        return cartService.addCart(itemRequestDto);
    }

    //CHECKOUT CART
    @PostMapping("/checkout-cart")
    public ResponseEntity checkoutCart(@RequestBody OrderRequestDto orderRequestDto) throws Exception {
        return cartService.checkoutCart(orderRequestDto);
    }

    //REMOVE FROM CART
    @DeleteMapping("/delete-from-cart/{customerId}/{itemId}")
    public ResponseEntity deleteFromCart(@PathVariable int customerId , @PathVariable int itemId) throws Exception {
        return cartService.deleteFromCart(customerId , itemId);
    }

    //VIEW ALL ITEMS IN CART
    @GetMapping("/view-customer-cart/{customerId}")
    public ResponseEntity viewEntireCart(@PathVariable int customerId){
        return cartService.viewEntireCart(customerId);
    }
}
