package com.example.Ecommerce.transformer;

import com.example.Ecommerce.dto.request.CustomerRequestDto;
import com.example.Ecommerce.dto.response.CustomerResponseDto;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.Customer;

public class CustomerTransformer {

    public static Customer CustomerRequestDtoToCustomer(CustomerRequestDto customerRequestDto){

        return Customer.builder()
                .name(customerRequestDto.getName())
                .age(customerRequestDto.getAge())
                .mobNo(customerRequestDto.getMobNo())
                .emailId(customerRequestDto.getEmailId())
                .address(customerRequestDto.getAddress())
                .build();
    }
    public static CustomerResponseDto CustomerToCustomerResponseDto(Customer customer){
        return CustomerResponseDto.builder()
                .name(customer.getName())
                .emailId(customer.getEmailId())
                .build();
    }
}
