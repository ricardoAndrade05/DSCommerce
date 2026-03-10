package com.pessoal.dscommerce.tests;

import java.util.ArrayList;
import java.util.List;

import com.pessoal.dscommerce.projections.UserDetailsProjection;

public class UserDetailsFactory {

	public static List<UserDetailsProjection> createCustomClientUser(String username) {
		List<UserDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsProjectionImpl(username, 1L, "ROLE_CLIENT",
				"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"));
		return list;
	}

	public static List<UserDetailsProjection> createCustomAdminUser(String username) {
		List<UserDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsProjectionImpl(username, 2L, "ROLE_ADMIN",
				"$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"));
		return list;
	}
	
	public static List<UserDetailsProjection> createCustomAdminClientUser(String username) {
		List<UserDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsProjectionImpl(username, 1L, "ROLE_CLIENT","$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"));
		list.add(new UserDetailsProjectionImpl(username, 2L, "ROLE_ADMIN","$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"));
		return list;
	}

}

class UserDetailsProjectionImpl implements UserDetailsProjection {

	private String username;
	private Long roleId;
	private String authority;
	private String password;

	public UserDetailsProjectionImpl(String username, Long roleId, String authority, String password) {
		this.username = username;
		this.roleId = roleId;
		this.authority = authority;
		this.password = password;
	}

	@Override
	public Long getRoleId() {
		return roleId;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
