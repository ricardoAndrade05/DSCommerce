package com.pessoal.dscommerce.services;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.services.exceptions.ForbiddenException;
import com.pessoal.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

	@InjectMocks
	private AuthService authService;
	
	@Mock
	private UserService userService;
	
	private User admin;
	private User selfClient;
	private User otherClient;
	
	@BeforeEach
	public void setUp() {
		admin = UserFactory.createAdminUser();
		selfClient = UserFactory.createCustomClientUser(1L,"Bob");
		otherClient = UserFactory.createCustomClientUser(2L,"Maria");
	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenAdminLogged() {
		Mockito.when(userService.authenticated()).thenReturn(admin);
		
		Long userId = admin.getId();
		
		Assertions.assertDoesNotThrow(() -> authService.validateSelfOrAdmin(userId));
	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId = selfClient.getId();
		
		Assertions.assertDoesNotThrow(() -> authService.validateSelfOrAdmin(userId));
	}
	
	@Test
	public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenOtherUserLogged() {
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId = otherClient.getId();
		
		Assertions.assertThrows(ForbiddenException.class, () -> authService.validateSelfOrAdmin(userId));
	}
	
}
