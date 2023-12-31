package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.ProductRequestDto;
import com.example.Ecommerce.dto.response.ProductResponseDto;
import com.example.Ecommerce.enums.ProductCategory;
import com.example.Ecommerce.exception.ProductNotAvailable;
import com.example.Ecommerce.exception.SellerNotFound;
import com.example.Ecommerce.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    public ResponseEntity addProduct(ProductRequestDto productRequestDto) throws SellerNotFound;

    public List<ProductResponseDto> getAllProductsByCategory(ProductCategory category);

    public List<ProductResponseDto> getAllProductsByPriceAndCategory(int price, String category);

    public ResponseEntity getAllProductsBySeller(String emailId) throws SellerNotFound;

    public ResponseEntity deleteProduct(int sellerId, int productId) throws SellerNotFound, ProductNotAvailable;

    public ResponseEntity getTopFiveCheap();

    public ResponseEntity getTopFiveCostliest();

    public ResponseEntity outOfStockProducts();

    public ResponseEntity getAllAvailable();

    public ResponseEntity productsLessThanQuantity(int quantity);

    public ResponseEntity cheapestProducts(ProductCategory category);

    public ResponseEntity costliestProducts(ProductCategory category);
}
