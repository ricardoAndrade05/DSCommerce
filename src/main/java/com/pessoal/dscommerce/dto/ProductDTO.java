package com.pessoal.dscommerce.dto;

import com.pessoal.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.name = entity.getName();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
	}

}
