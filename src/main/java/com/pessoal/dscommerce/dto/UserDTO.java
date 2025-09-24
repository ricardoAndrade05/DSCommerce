package com.pessoal.dscommerce.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pessoal.dscommerce.entities.User;

public class UserDTO {
	
	private Long id;
	private String email;
	private String name;
	private String phone;
	private LocalDate birthDate;
	
	private List<String>roles = new ArrayList<>();

	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.phone = user.getPhone();
		this.birthDate = user.getBirthDate();
		this.roles = user.getRoles().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public List<String> getRoles() {
		return roles;
	}

}
