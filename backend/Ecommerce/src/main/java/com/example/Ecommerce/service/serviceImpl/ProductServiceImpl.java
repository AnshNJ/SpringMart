package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.ProductRequestDto;
import com.example.Ecommerce.dto.response.ProductResponseDto;
import com.example.Ecommerce.enums.ProductCategory;
import com.example.Ecommerce.enums.ProductStatus;
import com.example.Ecommerce.exception.ProductNotAvailable;
import com.example.Ecommerce.exception.SellerNotFound;
import com.example.Ecommerce.model.Item;
import com.example.Ecommerce.model.Product;
import com.example.Ecommerce.model.Seller;
import com.example.Ecommerce.repository.ProductRepository;
import com.example.Ecommerce.repository.SellerRepository;
import com.example.Ecommerce.service.ProductService;
import com.example.Ecommerce.transformer.ProductTransformer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    SellerRepository sellerRepo;

    @Autowired
    ProductRepository productRepo;

    public ResponseEntity addProduct(ProductRequestDto productRequestDto) throws SellerNotFound {

        Seller seller;
        try{
            seller =  sellerRepo.findById(productRequestDto.getSellerId()).get();
        }
        catch (Exception e) {
            return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);
        }

        Product product = ProductTransformer.ProductRequestDtoToProduct(productRequestDto);
        product.setSeller(seller);

        // add product to current products of seller
        seller.getProducts().add(product);
        sellerRepo.save(seller);  // saves both seller and product

        // prepare Response Dto
        return new ResponseEntity(ProductTransformer.ProductToProductResponseDto(product) , HttpStatus.CREATED);
    }

    @Override
    public List<ProductResponseDto> getAllProductsByCategory(ProductCategory category) {
        List<Product> productList = productRepo.findAllByProductCategory(category);

        //Prepare response
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product: productList){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }

        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getAllProductsByPriceAndCategory(int price, String category) {
        //Requires custom query
        List<Product> productList = productRepo.findAllByPriceAndCategory(price , category);

        //Prepare response
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product: productList){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }

        return productResponseDtos;
    }

    @Override
    public ResponseEntity getAllProductsBySeller(String emailId) throws SellerNotFound {
        Seller seller;
        seller = sellerRepo.findByEmailId(emailId);
        if(seller == null) return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);

        List<Product> productList = seller.getProducts();

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(Product product : productList){
            ProductResponseDto productResponseDto = ProductTransformer.ProductToProductResponseDto(product);
            productResponseDtoList.add(productResponseDto);
        }

        return new ResponseEntity(productResponseDtoList , HttpStatus.FOUND);
    }

    @Transactional
    @Override
    public ResponseEntity deleteProduct(int sellerId, int productId) throws SellerNotFound, ProductNotAvailable {
        Seller seller;
        try{
            seller =  sellerRepo.findById(sellerId).get();
        }
        catch (Exception e) {
            return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);
        }

        Product product;
        try{
            product =  productRepo.findById(productId).get();
        }
        catch (Exception e) {
            return new ResponseEntity(new ProductNotAvailable("Product Not Available").getMessage(), HttpStatus.FOUND);
        }

        List<Product> productList = seller.getProducts();
        productList.remove(product);
        productRepo.deleteById(productId);

        ProductResponseDto productResponseDto = ProductTransformer.ProductToProductResponseDto(product);

        return new ResponseEntity(productResponseDto , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getTopFiveCheap() {
        List<Product> productList = productRepo.findAll();

        //Using minHeap
        PriorityQueue<Product> pq = new PriorityQueue<>((a,b) -> {
            return a.getPrice() - b.getPrice();
        });

        for(Product product : productList){
            pq.add(product);
        }

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(int i=0;i<5;i++){
            if(pq.isEmpty()) break;
            Product product = pq.remove();
            productResponseDtoList.add(ProductTransformer.ProductToProductResponseDto(product));
        }
        return new ResponseEntity(productResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getTopFiveCostliest() {
        List<Product> productList = productRepo.findAll();

        //Using minHeap
        PriorityQueue<Product> pq = new PriorityQueue<>((a,b) -> {
            return b.getPrice() - a.getPrice();
        });

        for(Product product : productList){
            pq.add(product);
        }

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(int i=0;i<5;i++){
            if(pq.isEmpty()) break;
            Product product = pq.remove();
            productResponseDtoList.add(ProductTransformer.ProductToProductResponseDto(product));
        }
        return new ResponseEntity(productResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity outOfStockProducts() {
        List<Product> productList = productRepo.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(Product product : productList){
            if(product.getProductStatus() == ProductStatus.OUT_OF_STOCK){
                productResponseDtoList.add(ProductTransformer.ProductToProductResponseDto(product));
            }
        }
        return new ResponseEntity(productResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getAllAvailable() {
        List<Product> productList = productRepo.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(Product product : productList){
            if(product.getProductStatus() == ProductStatus.AVAILABLE){
                productResponseDtoList.add(ProductTransformer.ProductToProductResponseDto(product));
            }
        }
        return new ResponseEntity(productResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity productsLessThanQuantity(int quantity) {
        List<Product> productList = productRepo.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(Product product : productList){
            if(product.getQuantity() < quantity ){
                productResponseDtoList.add(ProductTransformer.ProductToProductResponseDto(product));
            }
        }
        return new ResponseEntity(productResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity cheapestProducts(ProductCategory category) {
        List<Product> productList = productRepo.findAllByProductCategory(category);
        if(productList.isEmpty()) return new ResponseEntity("No products found in this category" , HttpStatus.BAD_REQUEST);
        Product cheapestProd = null;
        for(Product product : productList){
            if(cheapestProd != null && product.getPrice() < cheapestProd.getPrice()){
                cheapestProd = product;
            } else if(cheapestProd == null){
                cheapestProd = product;
            }
        }

        return new ResponseEntity(ProductTransformer.ProductToProductResponseDto(cheapestProd) , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity costliestProducts(ProductCategory category) {
        List<Product> productList = productRepo.findAllByProductCategory(category);
        if(productList.isEmpty()) return new ResponseEntity("No products found in this category" , HttpStatus.BAD_REQUEST);
        Product cheapestProd = null;
        for(Product product : productList){
            if(cheapestProd != null && product.getPrice() > cheapestProd.getPrice()){
                cheapestProd = product;
            } else if(cheapestProd == null){
                cheapestProd = product;
            }
        }

        return new ResponseEntity(ProductTransformer.ProductToProductResponseDto(cheapestProd) , HttpStatus.FOUND);
    }

    public void decreaseProductQuantity(Item item) throws Exception {
        Product product = item.getProduct();
        int quantity = item.getRequiredQuantity();
        int currQuantity = product.getQuantity();
        if(quantity > currQuantity){
            throw new Exception("Out Of Stock!");
        }
        product.setQuantity(currQuantity - quantity);
        if(product.getQuantity() == 0) product.setProductStatus(ProductStatus.OUT_OF_STOCK);
    }
}
