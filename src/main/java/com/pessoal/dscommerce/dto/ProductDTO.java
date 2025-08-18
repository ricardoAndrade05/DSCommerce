package com.pessoal.dscommerce.dto;

import com.pessoal.dscommerce.entities.Product;

import lombok.Data;

@Data
public class ProductDTO {
	
	private Long id;
	private String description;
	private String name;
	private Double price;
	private String imgUrl;
	
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.name = entity.getName();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
	}

}
