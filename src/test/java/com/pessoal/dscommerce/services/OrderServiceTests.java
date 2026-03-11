package com.pessoal.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pessoal.dscommerce.dto.OrderDTO;
import com.pessoal.dscommerce.entities.Order;
import com.pessoal.dscommerce.entities.OrderItem;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.repositories.OrderItemRepository;
import com.pessoal.dscommerce.repositories.OrderRepository;
import com.pessoal.dscommerce.repositories.ProductRepository;
import com.pessoal.dscommerce.services.exceptions.ForbiddenException;
import com.pessoal.dscommerce.services.exceptions.ResourceNotFoundException;
import com.pessoal.dscommerce.tests.OrderFactory;
import com.pessoal.dscommerce.tests.ProductFactory;
import com.pessoal.dscommerce.tests.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

	@InjectMocks
	private OrderService service;

	@Mock
	private OrderRepository repository;

	@Mock
	private AuthService authService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private UserService userService;

	private Long existingOrderId, nonExistingOrderId;
	private Long existingProductId, nonExistingProductId;
	private Order order;
	private OrderDTO orderDTO;
	private User admin, client;
	private Product product;

	@BeforeEach
	void setUp() throws Exception {
		existingOrderId = 1L;
		nonExistingOrderId = 1000L;
		
		existingProductId = 1L;
		nonExistingProductId = 1000L;

		admin = UserFactory.createCustomAdminUser(1L, "Jeff");
		client = UserFactory.createCustomClientUser(2L, "Bob");

		order = OrderFactory.createOrder(client);

		orderDTO = new OrderDTO(order);
		
		product = ProductFactory.createProduct();

		Mockito.when(repository.findById(existingOrderId)).thenReturn(java.util.Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(any())).thenReturn(order);
		
		Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO result = service.findById(existingOrderId);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingOrderId, result.getId());

	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO result = service.findById(existingOrderId);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingOrderId, result.getId());
	}

	@Test
	public void findByIdShouldThrowForbiddenExceptionWhenIdExistsAndAnotherClientLogged() {
		Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

		Assertions.assertThrows(ForbiddenException.class, () -> {
			service.findById(existingOrderId);
		});
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingOrderId);
		});
	}
	
	@Test
	public void insertShouldReturnOrderDTOWhenAdminLogged() {
		Mockito.when(userService.authenticated()).thenReturn(admin);

		OrderDTO result = service.insert(orderDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingOrderId, result.getId());	
	}
	
	@Test
	public void insertShouldReturnOrderDTOWhenClientLogged() {
		Mockito.when(userService.authenticated()).thenReturn(client);

		OrderDTO result = service.insert(orderDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingOrderId, result.getId());	
	}
	
	@Test
	public void insertShouldThrowEntityNotFoundExceptionnWhenProductDoesNotExist() {
		Mockito.when(userService.authenticated()).thenReturn(client);
		product.setId(nonExistingProductId);
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		orderDTO = new OrderDTO(order);
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.insert(orderDTO);
		});
	}
	
	

}
