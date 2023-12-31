package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.CustomerRequestDto;
import com.example.Ecommerce.exception.CustomerAlreadyPresent;
import com.example.Ecommerce.exception.CustomerNotFound;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    public ResponseEntity addCustomer(CustomerRequestDto customerRequestDto) throws CustomerAlreadyPresent;

    public ResponseEntity getAllCustomers();

    public ResponseEntity getCustomerByEmail(String emailId) throws CustomerAlreadyPresent, CustomerNotFound;

    public ResponseEntity getCustomerById(Integer customerId) throws CustomerNotFound;

    public ResponseEntity deleteCustomer(Integer customerId) throws CustomerNotFound;

    public ResponseEntity deleteAllCustomers();
}
