package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {
	private ItemController itemcontroller;
	private ItemRepository itemRepo = mock(ItemRepository.class);

	@Before
	public void setUp() {
		itemcontroller = new ItemController();
		TestUtils.injectObjects(itemcontroller, "itemRepository", itemRepo);
		when(itemRepo.findAll()).thenReturn(getItems());
		when(itemRepo.findById((long) 1)).thenReturn(Optional.of(getItems().get(0)));
		when(itemRepo.findByName("testItem1")).thenReturn(getItemsByName());
	}

	@Test
	public void getAllItems() {
		ResponseEntity<List<Item>> response = itemcontroller.getItems();
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();
		assertNotNull(items);
		assertEquals("testItem1", items.get(0).getName());

	}

	@Test
	public void getItemById() {
		ResponseEntity<Item> response = itemcontroller.getItemById((long) 1);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		Item item = response.getBody();
		assertNotNull(item);
		assertEquals("testItem1", item.getName());

	}

	@Test
	public void getByName() {
		ResponseEntity<List<Item>> response = itemcontroller.getItemsByName("testItem1");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();
		assertNotNull(items);
		assertEquals("testItem1", items.get(0).getName());

	}
	
	@Test
	public void getByInvalidName() {
		ResponseEntity<List<Item>> response = itemcontroller.getItemsByName("test");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	private List<Item> getItems() {
		List<Item> items = new ArrayList<>();
		items.add(new Item((long) 1, "testItem1", new BigDecimal(20), "bbbbb"));
		items.add(new Item((long) 2, "testItem2", new BigDecimal(20), "bbbbb"));
		items.add(new Item((long) 3, "testItem3", new BigDecimal(20), "bbbbb"));
		return items;

	}
	
	private List<Item> getItemsByName() {
		List<Item> items = new ArrayList<>();
		items.add(new Item((long) 1, "testItem1", new BigDecimal(20), "bbbbb"));
		return items;

	}

}
