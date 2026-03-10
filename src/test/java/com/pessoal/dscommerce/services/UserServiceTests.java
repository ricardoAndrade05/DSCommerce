package com.pessoal.dscommerce.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pessoal.dscommerce.dto.UserDTO;
import com.pessoal.dscommerce.entities.User;
import com.pessoal.dscommerce.projections.UserDetailsProjection;
import com.pessoal.dscommerce.repositories.UserRepository;
import com.pessoal.dscommerce.tests.UserDetailsFactory;
import com.pessoal.dscommerce.tests.UserFactory;
import com.pessoal.dscommerce.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil customUserUtil;

	private String existingUsername;
	private String nonExistingUsername;
	private User user;
	private List<UserDetailsProjection> userDetails;

	@BeforeEach
	void setUp() throws Exception {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "user@gmail.com";
		user = UserFactory.createCustomClientUser(1L, existingUsername);
		userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

		Mockito.when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(List.of());

		Mockito.when(repository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByEmail(nonExistingUsername)).thenReturn(Optional.empty());

	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUsername);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingUsername, result.getUsername());
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingUsername);
		});
	}

	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);

		User result = service.authenticated();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingUsername, result.getEmail());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}

	@Test
	public void getMeShouldReturnUserDTOWhenUserAuthenticated() {
		UserService spyService = Mockito.spy(service);
		Mockito.doReturn(user).when(spyService).authenticated();

		UserDTO result = spyService.getMe();
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingUsername, result.getEmail());

	}
	
	@Test
	public void getMeShouldThrowUsernameNotFoundExceptionWhenUserNotAuthenticated() {
		UserService spyService = Mockito.spy(service);
		Mockito.doThrow(UsernameNotFoundException.class).when(spyService).authenticated();

		Assertions.assertThrows(UsernameNotFoundException.class, () -> { 
			spyService.getMe();
		});
	}

}
