package com.pessoal.dscommerce.services;

import java.time.Instant;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscommerce.dto.OrderDTO;
import com.pessoal.dscommerce.dto.OrderItemDTO;
import com.pessoal.dscommerce.entities.Order;
import com.pessoal.dscommerce.entities.OrderItem;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.enums.OrderStatus;
import com.pessoal.dscommerce.repositories.OrderItemRepository;
import com.pessoal.dscommerce.repositories.OrderRepository;
import com.pessoal.dscommerce.repositories.ProductRepository;
import com.pessoal.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));
		return new OrderDTO(order);
	}

    @Transactional
	public OrderDTO insert(OrderDTO dto) {
		
    	Order order = new Order();
    	
    	order.setMoment(Instant.now());
    	order.setStatus(OrderStatus.WAITING_PAYMENT);
    	
    	order.setClient(userService.authenticated());
    	
    	for (OrderItemDTO itemDto : dto.getItems()) {
    		Product product = productRepository.getReferenceById(itemDto.getProductId());
    		OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
    		Set<OrderItem> items = order.getItems();
    		items.add(item);
    	}
    	
    	repository.save(order);
    	orderItemRepository.saveAll(order.getItems());
    	
    	return new OrderDTO(order);
	}
}
