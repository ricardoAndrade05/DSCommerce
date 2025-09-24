package com.pessoal.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscommerce.dto.CategoryDTO;
import com.pessoal.dscommerce.dto.ProductDTO;
import com.pessoal.dscommerce.dto.ProductMinDTO;
import com.pessoal.dscommerce.entities.Category;
import com.pessoal.dscommerce.entities.Product;
import com.pessoal.dscommerce.repositories.ProductRepository;
import com.pessoal.dscommerce.services.exceptions.DatabaseException;
import com.pessoal.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));
		return new ProductDTO(product);
	}

	@Transactional(readOnly = true)
	public Page<ProductMinDTO> findAll(String name,Pageable pageable) {
		Page<Product> result = repository.searchByName(name,pageable);
		return result.map(p -> new ProductMinDTO(p));
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
		try {
			Product product = repository.getReferenceById(id);
			copyDTOToEntity(dto, product);
			product = repository.save(product);
			return new ProductDTO(product);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDTOToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        
        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
        	Category cat = new Category();
        	cat.setId(catDto.getId());
        	entity.getCategories().add(cat);
        }
		
	}

}
