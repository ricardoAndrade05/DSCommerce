package com.pessoal.dscommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pessoal.dscommerce.repositories.ProductRepository;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private ProductRepository repository;
	
	@GetMapping
	public String teste() {
		return repository.findById(1L).get().getName();
	}

}
