package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.SellerRequestDto;
import com.example.Ecommerce.exception.EmailAlreadyPresent;
import com.example.Ecommerce.exception.SellerNotFound;
import org.springframework.http.ResponseEntity;

public interface SellerService {
    public ResponseEntity addSeller(SellerRequestDto sellerRequestDto) throws Exception;

    public ResponseEntity getSellerByEmail(String emailId) throws SellerNotFound;

    public ResponseEntity getSellerById(Integer id) throws SellerNotFound;

    public ResponseEntity getAllSellers();

    public ResponseEntity deleteSellerByEmail(String emailId) throws SellerNotFound;

    public ResponseEntity updateSellerUsingEmail(SellerRequestDto sellerRequestDto) throws SellerNotFound;
}
