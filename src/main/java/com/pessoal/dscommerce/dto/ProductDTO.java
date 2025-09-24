package com.pessoal.dscommerce.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pessoal.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {

	private Long id;

	@NotBlank(message = "Campo requerido.")
	@Size(min = 3, message = "Descrição precisa ter no mínimo 10 caracteres.")
	private String description;

	@NotBlank(message = "Campo requerido.")
	@Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres.")
	private String name;

	@Positive(message = "O preço deve ser positivo.")
	private Double price;
	private String imgUrl;

	@NotEmpty(message = "Deve ter ao menos uma categoria.")
	private List<CategoryDTO> categories = new ArrayList<>();

	public ProductDTO() {
	}

	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.name = entity.getName();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.categories = entity.getCategories().stream().map(category -> new CategoryDTO(category))
				.collect(Collectors.toList());
	}

	public ProductDTO(Long id, String description, String name, Double price, String imgUrl) {
		this.id = id;
		this.description = description;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

}
