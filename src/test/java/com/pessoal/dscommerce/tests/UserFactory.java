package com.pessoal.dscommerce.tests;

import java.time.LocalDate;

import com.pessoal.dscommerce.entities.Role;
import com.pessoal.dscommerce.entities.User;

public class UserFactory {

	public static User createClientUser() {
		User user = new User(1L, "maria@gmail.com", "Maria", "988888888", LocalDate.parse("2001-07-25"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		return user;
	}
	
	public static User createAdminUser() {
		User user = new User(2L, "Alex@gmail.com", "Alex", "988888888", LocalDate.parse("1987-12-13"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		return user;
	}
	
	public static User createCustomClientUser(Long id, String username) {
		User user = new User(id, username, "Maria", "988888888", LocalDate.parse("1987-12-13"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(2L, "ROLE_CLIENT"));
		return user;
	}
	
	public static User createCustomAdminUser(Long id, String username) {
		User user = new User(id, username, "Alex", "988888888", LocalDate.parse("1987-12-13"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		return user;
	}
}
