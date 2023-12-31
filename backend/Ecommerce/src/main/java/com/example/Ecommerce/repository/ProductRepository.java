package com.example.Ecommerce.repository;

import com.example.Ecommerce.enums.ProductCategory;
import com.example.Ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByProductCategory(ProductCategory category);

    //Executing manual query
    @Query(value = "select * from product p where p.price < :price and p.product_category = :category" , nativeQuery = true)
    List<Product> findAllByPriceAndCategory(int price , String category);
}
