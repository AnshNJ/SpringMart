package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.request.SellerRequestDto;
import com.example.Ecommerce.exception.EmailAlreadyPresent;
import com.example.Ecommerce.exception.SellerNotFound;
import com.example.Ecommerce.service.SellerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    //ADD SELLER
    @PostMapping("/add-seller")
    public ResponseEntity addSeller(@RequestBody SellerRequestDto sellerRequestDto) throws Exception {
        return sellerService.addSeller(sellerRequestDto);
    }

    //GET SELLER USING THEIR EMAIL ID
    @GetMapping("/get-seller-by-email")
    public ResponseEntity getSellerByEmail(@RequestParam String emailId) throws SellerNotFound {
        return sellerService.getSellerByEmail(emailId);
    }

    //GET SELLER USING THEIR ID
    @GetMapping("/get-seller-by-id")
    public ResponseEntity getSellerById(@RequestParam Integer id) throws SellerNotFound {
        return sellerService.getSellerById(id);
    }

    //GET ALL SELLERS
    @GetMapping("/get-all-sellers")
    public ResponseEntity getAllSellers(){
        return sellerService.getAllSellers();
    }

    //DELETE A STUDENT BY EMAIL
    @DeleteMapping("/delete-seller-by-email")
    public ResponseEntity deleteSellerByEmail(@RequestParam String emailId) throws SellerNotFound {
        return sellerService.deleteSellerByEmail(emailId);
    }

    //UPDATE SELLER INFO USING EMAIL
    @PutMapping("/update-seller")
    public ResponseEntity updateSellerUsingEmail(@RequestBody SellerRequestDto sellerRequestDto) throws SellerNotFound {
        return sellerService.updateSellerUsingEmail(sellerRequestDto);
    }

}
