package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.request.CustomerRequestDto;
import com.example.Ecommerce.dto.response.CustomerResponseDto;
import com.example.Ecommerce.exception.CustomerAlreadyPresent;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.service.CustomerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //ADD CUSTOMER
    @PostMapping("/add-customer")
    public ResponseEntity addCustomer(@RequestBody CustomerRequestDto customerRequestDto) throws CustomerAlreadyPresent {
        return customerService.addCustomer(customerRequestDto);
    }

    //VIEW ALL CUSTOMERS
    @GetMapping("/get-all-customers")
    public ResponseEntity getAllCustomers(){
        return customerService.getAllCustomers();
    }

    //GET A CUSTOMER BY EMAIL
    @GetMapping("/get-customer-by-email")
    public ResponseEntity getCustomerByEmail(@RequestParam String emailId) throws CustomerAlreadyPresent, CustomerNotFound {
        return customerService.getCustomerByEmail(emailId);
    }

    //Find by ID
    @GetMapping("/get-customer-by-id")
    public ResponseEntity getCustomerById(@RequestParam Integer customerId) throws CustomerAlreadyPresent, CustomerNotFound {
        return customerService.getCustomerById(customerId);
    }

    //DELETE CUSTOMER BY ID
    @DeleteMapping("/delete-customer-by-id")
    public ResponseEntity deleteCustomer(@RequestParam Integer customerId) throws CustomerNotFound {
        return customerService.deleteCustomer(customerId);
    }

    //DELETE ALL CUSTOMERS
    @DeleteMapping("/delete-all-customers")
    public ResponseEntity deleteAllCustomers(){
        return customerService.deleteAllCustomers();
    }
}
