package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("mockUser");
        user.setPassword("mockPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("mockUser")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId((long)1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("mockUser");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertEquals(BigDecimal.valueOf(2.99), cart.getTotal());
    }

    @Test
    public void add_to_cart_error_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId((long)1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("mock-user");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId((long)1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("mockUser");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId((long)1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("mockUser");
        response = cartController.removeFromcart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertEquals(0, cart.getTotal().intValue());
    }

    @Test
    public void remove_from_cart_error_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId((long)1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("mock-user");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId((long)1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("mock-user");
        response = cartController.removeFromcart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

}
