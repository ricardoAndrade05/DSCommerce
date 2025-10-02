package com.pessoal.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	public void validateSelfOrAdmin(long userId) {
		User me = userService.authenticated();
		if (!me.hasHole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Acesso negado");
		}
		
	}

}
