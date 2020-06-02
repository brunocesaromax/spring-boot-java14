package com.example.Java14SpringBoot.repository;

import com.example.Java14SpringBoot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
