package com.pessoal.dscommerce.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pessoal.dscommerce.dto.ProductDTO;
import com.pessoal.dscommerce.dto.ProductMinDTO;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.repositories.ProductRepository;
import com.pessoal.dscommerce.services.exceptions.ResourceNotFoundException;
import com.pessoal.dscommerce.tests.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private String productName;
	private Product product;
	private PageImpl<Product> page;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		productName = "Playstation 5";

		product = ProductFactory.createProduct(productName);
		
		page = new PageImpl<>(List.of(product));

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.searchByName(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getName(), product.getName());

	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void findAllShouldReturnListOfProductMinDTO() {
		Pageable pageable = PageRequest.of(0, 10);
		String name = "Playstation 5";
		
		Page<ProductMinDTO> result = service.findAll(name, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(name, result.getContent().get(0).getName());

	}

}
