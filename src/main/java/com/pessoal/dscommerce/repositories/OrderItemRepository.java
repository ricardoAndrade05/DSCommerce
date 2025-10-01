package com.pessoal.dscommerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pessoal.dscommerce.entities.OrderItem;
import com.pessoal.dscommerce.entities.OrderItemPk;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPk> {
}
