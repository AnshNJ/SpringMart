package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.request.DirectOrderRequestDto;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.service.OrderService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    //PLACE DIRECT ORDER
    @PostMapping("/place-order")
    public ResponseEntity placeOrder(@RequestBody DirectOrderRequestDto directOrderRequestDto) throws Exception {
        return orderService.placeOrder(directOrderRequestDto);
    }

    //GET ALL ORDERS FOR A CUSTOMER
    @GetMapping("/get-all-orders-from-customer/{customerId}")
    public ResponseEntity getAllOrdersFromCustomer(@PathVariable int customerId) throws CustomerNotFound {
        return orderService.getAllOrdersFromCustomer(customerId);
    }

    //GET RECENT n ORDERS
    @GetMapping("/get-recent-orders/{numberOfOrders}")
    public ResponseEntity getRecentOrders(@PathVariable int numberOfOrders){
        return orderService.getRecentOrders(numberOfOrders);
    }

    //ORDER WITH HIGHEST TOTAL VALUE FOR A CUSTOMER
    @GetMapping("/highest-value-order/{customerid}")
    public ResponseEntity highestValueOrder(@PathVariable int customerId) throws CustomerNotFound {
        return orderService.highestValueOrder(customerId);
    }


}
