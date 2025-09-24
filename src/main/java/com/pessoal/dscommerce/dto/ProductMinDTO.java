package com.pessoal.dscommerce.dto;

import java.util.Objects;

import com.pessoal.dscommerce.entities.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMinDTO {

	private Long id;
	private String name;
	private Double price;
	private String imgUrl;

	public ProductMinDTO(Long id, String name, Double price, String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
	}

	public ProductMinDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductMinDTO other = (ProductMinDTO) obj;
		return Objects.equals(id, other.id);
	}
	
}
