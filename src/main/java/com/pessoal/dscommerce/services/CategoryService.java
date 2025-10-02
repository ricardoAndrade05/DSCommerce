package com.pessoal.dscommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscommerce.dto.CategoryDTO;
import com.pessoal.dscommerce.entities.Category;
import com.pessoal.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category>result = repository.findAll();
		return result.stream().map(p -> new CategoryDTO(p)).collect(Collectors.toList());
	}
}
