package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class CartControllerTest {
	private CartController cartController;
	private UserRepository userRepo = mock(UserRepository.class);
	private CartRepository cartRepo = mock(CartRepository.class);
	private ItemRepository itemRepo= mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepo);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepo);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepo);
        when(userRepo.findByUsername("testUser")).thenReturn(createUserWithCart());
        when(itemRepo.findById((long) 1)).thenReturn(Optional.of(new Item((long) 1,"item1",new BigDecimal(1),"ddd")));

        

	}
	
	private User createUserWithCart() {
		User user = new User();
		user.setCart(new Cart());
		return user;
	}

	@Test
	public void addToCart() {
		ModifyCartRequest cr = new ModifyCartRequest();
		cr.setUsername("testUser");
		cr.setQuantity(3);
		cr.setItemId(1);
		ResponseEntity<Cart> response = cartController.addTocart(cr);
		assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	public void removeFromCart() {
		ModifyCartRequest cr = new ModifyCartRequest();
		cr.setUsername("testUser");
		cr.setQuantity(3);
		cr.setItemId(1);
		ResponseEntity<Cart> response = cartController.removeFromcart(cr);
		assertEquals(200, response.getStatusCodeValue());
	}
}
