package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {
	private UserController userController;
	private UserRepository userRepo = mock(UserRepository.class);
	private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

	
	@Before
	public void setUp() {
		userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepo);
        TestUtils.injectObjects(userController,"cartRepository",cartRepo);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
        when(encoder.encode(any())).thenReturn("thisIsHashed");
        when(userRepo.findById((long)1)).thenReturn(Optional.of(createUser()));
        when(userRepo.findByUsername("test")).thenReturn(createUser());

	}
	
	@Test
	public void createUserHappyPath() {
		CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testpassword");
        r.setConfirmPassword("testpassword");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());
	}
	
	@Test
	public void createUserPasswordNotMatch() {
		CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testpassword");
        r.setConfirmPassword("test21");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
	}
	@Test
	public void createUserPasswordLength() {
		CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testp");
        r.setConfirmPassword("testp");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
	}
	
	@Test
	public void getUserById() {
		User u = createUser();
        assertNotNull(u);
        ResponseEntity<User> foundUserResponse = userController.findById((long) 1);
        assertEquals(1, foundUserResponse.getBody().getId());
        assertEquals("test",foundUserResponse.getBody().getUsername());        
	}
	
	@Test
	public void getUserByUsername() {
		User u = createUser();
        assertNotNull(u);
        ResponseEntity<User> foundUserResponse = userController.findByUserName("test");
        assertEquals(1, foundUserResponse.getBody().getId());
        assertEquals("test",foundUserResponse.getBody().getUsername());        
	}
	
	@Test
	public void getUserByInvalidUsername() {
		User u = createUser();
        assertNotNull(u);
        ResponseEntity<User> foundUserResponse = userController.findByUserName("aa");
        assertEquals(404, foundUserResponse.getStatusCodeValue());
	}
	
	private User createUser() {
        User user = new User();
        user.setUsername("test");
        user.setId(1);
        return user;
		
	}

}
