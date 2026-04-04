package com.pessoal.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pessoal.dscommerce.dto.ProductDTO;
import com.pessoal.dscommerce.entities.Category;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingProductId,nonExistingProductId,dependentProductId;
	private String clientUserName, clientPassword, adminUserName, adminPassword;
	private String clienteToken,adminToken,invalidToken;	

	private String productName;
	
	private ProductDTO productDTO;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		clientUserName = "maria@gmail.com";
		clientPassword = "123456";
		
		adminUserName = "alex@gmail.com";
		adminPassword = "123456";
		
		productName = "Macbook";
		existingProductId = 2L;
		nonExistingProductId = 1000L;
		dependentProductId = 3L;
	
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUserName, adminPassword);
		clienteToken = tokenUtil.obtainAccessToken(mockMvc, clientUserName, clientPassword);
		invalidToken = adminToken + "xpto";
		
		Category category = new Category(2L, "Eletrônicos");
		product = new Product(null, "Playstation 5", "Lorem ipsum dolor sit amet, consectetur.", 3999.00, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg");
		product.getCategories().add(category); 
		
		productDTO = new ProductDTO(product);
		
	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {
		ResultActions result = mockMvc.perform(get("/products?name={productName}",productName).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.00));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
		
	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
		
	}
	
	@Test
	public void insertShouldReturnProductDTOWhenAdminLogged() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization","Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(26L));
		result.andExpect(jsonPath("$.name").value("Playstation 5"));
		result.andExpect(jsonPath("$.description").value("Lorem ipsum dolor sit amet, consectetur."));
		result.andExpect(jsonPath("$.price").value(3999.00));
		result.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
		result.andExpect(jsonPath("$.categories[0].id").value(2L));
		
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {
		
		product.setName("ab");
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization","Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {
		
		product.setDescription("ab");
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization","Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {
		
		product.setPrice(-10.0);
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization","Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {
		
		product.setPrice(0.0);
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization","Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndProductHasNoCategory() throws Exception {
		
		product.getCategories().clear();
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization","Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization", "Bearer " + clienteToken).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());

		result.andExpect(status().isForbidden());

	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products")
				.header("Authorization", "Bearer " + invalidToken).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());

		result.andExpect(status().isUnauthorized());

	}
	
	@Test
	public void deleteShouldReturnNoContentWhenAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", existingProductId)
				.header("Authorization","Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON));
				
		
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdNonExistingWhenAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", nonExistingProductId)
				.header("Authorization","Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON));
				
		
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteShouldReturnBadRequestWhenIdExistingWhenAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", dependentProductId)
				.header("Authorization","Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON));
				
		
		result.andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenClientLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", dependentProductId)
				.header("Authorization","Bearer " + clienteToken)
				.accept(MediaType.APPLICATION_JSON));
				
		
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", dependentProductId)
				.header("Authorization","Bearer " + invalidToken)
				.accept(MediaType.APPLICATION_JSON));
				
		
		result.andExpect(status().isUnauthorized());
		
	}
}
