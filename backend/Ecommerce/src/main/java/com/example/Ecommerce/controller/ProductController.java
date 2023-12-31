package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.request.ProductRequestDto;
import com.example.Ecommerce.dto.response.ProductResponseDto;
import com.example.Ecommerce.enums.ProductCategory;
import com.example.Ecommerce.exception.ProductNotAvailable;
import com.example.Ecommerce.exception.SellerNotFound;
import com.example.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    //ADD PRODUCT
    @PostMapping("/add-product")
    public ResponseEntity addProduct(@RequestBody ProductRequestDto productRequestDto) throws SellerNotFound {
        return productService.addProduct(productRequestDto);
    }

    //GET ALL PRODUCTS OF A PARTICULAR CATEGORY
    @GetMapping("/get/{category}")
    public List<ProductResponseDto> getAllProductsByCategory(@PathVariable("category") ProductCategory category){
        return productService.getAllProductsByCategory(category);
    }

    //GET ALL PRODUCTS BY PRICE AND CATEGORY
    @GetMapping("/get/{price}/{category}")
    public List<ProductResponseDto> getAllProductsByPriceAndCategory(
            @PathVariable("price") int price,
            @PathVariable("category") String category){

        return productService.getAllProductsByPriceAndCategory(price, category);
    }

    // Get all product by seller email id
    @GetMapping("/get-product-by-seller")
    public ResponseEntity getAllProductsBySeller(@RequestParam String emailId) throws SellerNotFound {
        return productService.getAllProductsBySeller(emailId);
    }

    // delete a product by seller id and product id
    @DeleteMapping("/delete-product/{sellerId}/{productId}")
    public ResponseEntity deleteProduct(@PathVariable int sellerId , @PathVariable int productId ) throws ProductNotAvailable, SellerNotFound {
        return productService.deleteProduct(sellerId, productId);
    }

    // return top 5 cheapest products
    @GetMapping("/get-top-five-cheap")
    public ResponseEntity getTopFiveCheap(){
        return productService.getTopFiveCheap();
    }

    // return top 5 costliest products
    @GetMapping("/get-top-five-costliest")
    public ResponseEntity getTopFiveCostliest(){
        return productService.getTopFiveCostliest();
    }

    // return all out of stock products
    @GetMapping("/out-of-stock")
    public ResponseEntity outOfStockProducts(){
        return productService.outOfStockProducts();
    }

    // return all available products
    @GetMapping("/get-all-available")
    public ResponseEntity getAllAvailable(){
        return productService.getAllAvailable();
    }

    // return all products that have quantity less than given quantity
    @GetMapping("/products-less-than-quantity/{quantity}")
    public ResponseEntity productsLessThanQuantity(@PathVariable int quantity){
        return productService.productsLessThanQuantity(quantity);
    }

    // return the cheapest product in a particular category
    @GetMapping("/cheapest-product/{category}")
    public ResponseEntity cheapestProducts(@PathVariable ProductCategory category){
        return productService.cheapestProducts(category);
    }

    // return the costliest product in a particular category
    @GetMapping("/costliest-product/{category}")
    public ResponseEntity costliestProducts(@PathVariable ProductCategory category){
        return productService.costliestProducts(category);
    }


}
