package com.example.E_ecommerce.repository;

import com.example.E_ecommerce.dto.ProductDTO;
import com.example.E_ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContaining(String title);
}
