package com.example.demo.controllers;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("User: "+username+" not found !!");
			return ResponseEntity.notFound().build();
		}
		else {
			logger.info("Fetching User: "+ username);
			return  ResponseEntity.ok(user);
		}

	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		logger.info("Creating User: "+createUserRequest.getUsername());
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		String salt = createSalt();
		user.setSalt(salt);
		if(createUserRequest.getPassword().length() < 8 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			logger.error("Password entered for new user '"+createUserRequest.getUsername()+"' doesn't match the requirments");
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()+salt));
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		logger.info("User: "+createUserRequest.getUsername()+" created sucessfully");
		return ResponseEntity.ok(user);
	}
	
	// Method to generate a Salt
	private static String createSalt() {
	  SecureRandom random = new SecureRandom();
	  byte[] salt = new byte[16];
	  random.nextBytes(salt);
	  return Base64.getEncoder().encodeToString(salt);
	 }
	
}
