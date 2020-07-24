package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Item item = new Item();
        item.setId((long)1);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId((long)1);
        user.setUsername("mockUser");
        user.setPassword("mockPassword");
        cart.setId((long)1);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepository.findByUsername("mockUser")).thenReturn(user);
    }

    @Test
    public void submit_happy_path() {
        ResponseEntity<UserOrder> response = orderController.submit("mockUser");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submit_error_path() {
        ResponseEntity<UserOrder> response = orderController.submit("mock-user");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_happy_path() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("mockUser");
        assertEquals(200, ordersForUser.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_error_path() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("mock-user");
        assertEquals(404, ordersForUser.getStatusCodeValue());
    }
}
