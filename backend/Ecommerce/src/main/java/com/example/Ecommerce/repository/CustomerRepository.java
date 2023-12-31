package com.example.Ecommerce.repository;

import com.example.Ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer, Integer> {
    Customer findByMobNo(String mobNo);
    Customer findByEmailId(String emailId);
}
