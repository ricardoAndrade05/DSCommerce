package com.pessoal.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pessoal.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
