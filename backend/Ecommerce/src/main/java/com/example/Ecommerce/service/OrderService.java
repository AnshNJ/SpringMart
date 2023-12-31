package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.DirectOrderRequestDto;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.model.Card;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.Customer;
import com.example.Ecommerce.model.Ordered;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public Ordered placeOrder(Customer customer, Card card, Cart cart) throws Exception;
    public ResponseEntity placeOrder(DirectOrderRequestDto directOrderRequestDto) throws Exception;

    public ResponseEntity getAllOrdersFromCustomer(int customerId) throws CustomerNotFound;

    public ResponseEntity getRecentOrders(int numberOfOrders);

    public ResponseEntity highestValueOrder(int customerId) throws CustomerNotFound;
}
