package com.example.Ecommerce.repository;

import com.example.Ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {

    Seller findByEmailId(String email);

    void deleteByEmailId(String email);
}
