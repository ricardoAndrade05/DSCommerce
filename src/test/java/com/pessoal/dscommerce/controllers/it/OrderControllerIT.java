package com.pessoal.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pessoal.dscommerce.dto.OrderDTO;
import com.pessoal.dscommerce.entities.Order;
import com.pessoal.dscommerce.entities.OrderItem;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.enums.OrderStatus;
import com.pessoal.dscommerce.tests.ProductFactory;
import com.pessoal.dscommerce.tests.TokenUtil;
import com.pessoal.dscommerce.tests.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private String clientUserName, clientPassword, adminUserName, adminPassword;
	private String clienteToken,adminToken,invalidToken;	
	private Long existingOrderId, nonExistingOrderId;
	
	private OrderDTO orderDTO;
	private Order order;
	
	private User user;
	
	@BeforeEach
	void setUp() throws Exception {
		clientUserName = "maria@gmail.com";
		clientPassword = "123456";
		adminUserName = "alex@gmail.com";
		adminPassword = "123456";
		
		existingOrderId = 1L;
		nonExistingOrderId = 1000L;
	
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUserName, adminPassword);
		clienteToken = tokenUtil.obtainAccessToken(mockMvc, clientUserName, clientPassword);
		invalidToken = adminToken + "xpto";

		User user = UserFactory.createClientUser();
		order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingOrderId)
				.header("Authorization","Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingOrderId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingOrderId)
				.header("Authorization","Bearer " + clienteToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingOrderId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
		
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenIdExistsAndClientLoggedAndOrderDoesNotBelongUser() throws Exception {
		
		Long otherOrderId = 2L;
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", otherOrderId)
				.header("Authorization","Bearer " + clienteToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isForbidden());
			
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", nonExistingOrderId)
				.header("Authorization","Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndClientLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", nonExistingOrderId)
				.header("Authorization","Bearer " + clienteToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingOrderId)
				.header("Authorization","Bearer " + invalidToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnauthorized());
	}
	
	
}
