package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.CustomerRequestDto;
import com.example.Ecommerce.dto.response.CustomerResponseDto;
import com.example.Ecommerce.exception.CustomerAlreadyPresent;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.Customer;
import com.example.Ecommerce.repository.CustomerRepository;
import com.example.Ecommerce.service.CustomerService;
import com.example.Ecommerce.transformer.CustomerTransformer;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepo;
    @Override
    public ResponseEntity addCustomer(CustomerRequestDto customerRequestDto) throws CustomerAlreadyPresent {
        Customer customer;
        if(customerRepo.findByEmailId(customerRequestDto.getEmailId()) != null){
            return new ResponseEntity(new CustomerAlreadyPresent("Customer Already Present").getMessage() , HttpStatus.FOUND);
        }

        customer = CustomerTransformer.CustomerRequestDtoToCustomer(customerRequestDto);

        //Prepare cart
        Cart cart = Cart.builder()
                        .cartTotal(0)
                        .numberOfItems(0)
                        .customer(customer)
                        .build();
//        Cart cart = new Cart();
//        cart.setCartTotal(0);
//        cart.setNumberOfItems(0);
//        cart.setCustomer(customer);

        customer.setCart(cart);

        customerRepo.save(customer); //saves customer and cart

        CustomerResponseDto customerResponseDto = CustomerTransformer.CustomerToCustomerResponseDto(customer);

        return new ResponseEntity(customerResponseDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        List<CustomerResponseDto> customerResponseDtoList = new ArrayList<>();
        for(Customer customer : customers){
            CustomerResponseDto customerResponseDto = CustomerTransformer.CustomerToCustomerResponseDto(customer);
            customerResponseDtoList.add(customerResponseDto);
        }
        return new ResponseEntity(customerResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getCustomerByEmail(String emailId) throws CustomerNotFound {
        Customer customer;
        if(customerRepo.findByEmailId(emailId) == null){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.FOUND);
        }
        customer = customerRepo.findByEmailId(emailId);

        CustomerResponseDto customerResponseDto = CustomerTransformer.CustomerToCustomerResponseDto(customer);
        return new ResponseEntity(customerResponseDto , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getCustomerById(Integer customerId) throws CustomerNotFound {
        Customer customer;
        try{
            customer = customerRepo.findById(customerId).get();
        } catch(Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.FOUND);
        }

        CustomerResponseDto customerResponseDto = CustomerTransformer.CustomerToCustomerResponseDto(customer);
        return new ResponseEntity(customerResponseDto , HttpStatus.FOUND);

    }

    @Override
    public ResponseEntity deleteCustomer(Integer customerId) throws CustomerNotFound {
        Customer customer;
        try{
            customer = customerRepo.findById(customerId).get();
            customerRepo.deleteById(customerId);
        } catch (Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.FOUND);
        }

        return new ResponseEntity(CustomerTransformer.CustomerToCustomerResponseDto(customer) , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity deleteAllCustomers() {
        customerRepo.deleteAll();
        return new ResponseEntity("All Customer Deleted" , HttpStatus.ACCEPTED);
    }


}
