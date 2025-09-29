package com.pessoal.dscommerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pessoal.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
