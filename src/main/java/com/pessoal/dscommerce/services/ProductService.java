package com.pessoal.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscommerce.dto.ProductDTO;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = repository.findById(id).get();
		return new ProductDTO(product);
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> result = repository.findAll(pageable);
		return result.map(p -> new ProductDTO(p));
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product product = new Product();
		copyDTOToEntity(dto, product);
		product = repository.save(product);
		return new ProductDTO(product);

	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		Product product = repository.getReferenceById(id);
		copyDTOToEntity(dto, product);
		product = repository.save(product);
		return new ProductDTO(product);

	}
	
	@Transactional
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	private void copyDTOToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
	}

}
