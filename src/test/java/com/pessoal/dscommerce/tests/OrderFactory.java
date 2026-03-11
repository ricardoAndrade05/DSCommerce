package com.pessoal.dscommerce.tests;

import java.time.Instant;

import com.pessoal.dscommerce.entities.Order;
import com.pessoal.dscommerce.entities.OrderItem;
import com.pessoal.dscommerce.entities.Payment;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.enums.OrderStatus;

public class OrderFactory {
	
	public static Order createOrder(User client) {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		return order;
	}

}
