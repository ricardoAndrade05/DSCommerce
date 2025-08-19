package com.pessoal.dscommerce.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pessoal.dscommerce.dto.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String description;
	private String name;
	private Double price;
	private String imgUrl;

	@ManyToMany
	@JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	@OneToMany(mappedBy = "id.product")
	private Set<OrderItem> items = new HashSet<>();

	public List<Order> getOrders() {
		return items.stream().map(x -> x.getOrder()).toList();
	}

	public Product(ProductDTO dto) {
		this.description = dto.getDescription();
		this.name = dto.getName();
		this.price = dto.getPrice();
		this.imgUrl = dto.getImgUrl();
	}

}
