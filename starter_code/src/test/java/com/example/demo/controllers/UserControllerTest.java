package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("mockPassword")).thenReturn("hashedPassword");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("mockUser");
        userRequest.setPassword("mockPassword");
        userRequest.setConfirmPassword("mockPassword");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("mockUser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void create_user_error_path() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("mockUser");
        userRequest.setPassword("mockPass");
        userRequest.setConfirmPassword("mockPassword");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void find_user_happy_path() {
        User user = new User();
        user.setId((long)1);
        user.setUsername("mockUser");
        user.setPassword("mockPassword");

        when(userRepository.findByUsername("mockUser")).thenReturn(user);

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("mockUser");
        userRequest.setPassword("mockPassword");
        userRequest.setConfirmPassword("mockPassword");

        userController.createUser(userRequest);

        ResponseEntity<User> response = userController.findByUserName("mockUser");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockUser", user.getUsername());
    }

    @Test
    public void find_user_error_path() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("mockUser");
        userRequest.setPassword("mockPassword");
        userRequest.setConfirmPassword("mockPassword");

        ResponseEntity<User> findUser = userController.findByUserName("mock_user");

        assertNotNull(userController.findByUserName("mock_user"));
        assertEquals(HttpStatus.NOT_FOUND, findUser.getStatusCode());
    }

    @Test
    public void find_by_id_happy_path() {
        User user = new User();
        user.setId(1);
        user.setUsername("mockUser");
        user.setPassword("mockPassword");
        when(userRepository.findById((long)1)).thenReturn(java.util.Optional.of(user));

        ResponseEntity<User> response = userController.findById((long)1);

        assertNotNull(response);
        assertEquals("mockUser", response.getBody().getUsername());
    }

}

