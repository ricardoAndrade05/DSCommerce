package com.pessoal.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscommerce.dto.UserDTO;
import com.pessoal.dscommerce.entities.Role;
import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.projections.UserDetailsProjection;
import com.pessoal.dscommerce.repositories.UserRepository;
import com.pessoal.dscommerce.util.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private CustomUserUtil customUserUtil;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);

		if (result.size() == 0) {
			throw new UsernameNotFoundException("Usuario não encontrado");
		}

		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}

		return user;
	}

	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		return new UserDTO(user);
	}
	
	protected User authenticated() {
		try {
			String username = customUserUtil.getLoggedUsername();
			User user = repository.findByEmail(username).get();
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
		}
	}

}
