package com.project.blog.controllers;

import com.project.blog.dtos.*;
import com.project.blog.exceptions.ValidationException;
import com.project.blog.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    AutoCloseable autoCloseable;

    UserDto userDto;
    UserDto userDto2;
    RoleDto roleDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mock(UserService.class);
        roleDto = RoleDto.builder().id(1).role("ROLE_USER").build();
        Set<RoleDto> roles = new HashSet<>();
        roles.add(roleDto);
        userDto =  UserDto.builder().id(1L).name("abc").email("broker@gmail.com").password("$2a$12$nUtegoroBWJfTe/ODxLK9euSX4hrL5VSQMDh2zFuM/ac7hDoFceba").about("co-founder and CEO of an online stock brokerage").roles(roles).build();
        userDto2 = UserDto.builder().id(2L).name("Amitabh").password("$2a$12$RFc/WFmtYUPy1glxcAnu0.LTh7TFG/SSrnIsrGTdw5Tyi5fE/3NqK").about("I am Amitabh Bachchan an Indian film actor.").roles(roles).build();
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void createUserTest() {
        when(userService.createUser(userDto)).thenReturn(UserDtoResponse.builder().id(1L).name("abc").email("broker@gmail.com").about("co-founder and CEO of an online stock brokerage").build());
        assertThat(userController.createUser(userDto).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void existingUserCreateTest() {
        when(userService.createUser(userDto)).thenThrow(new ValidationException("User already exists"));
        assertThrows(ValidationException.class, () -> userController.createUser(userDto));
    }

    @Test
    void updateUserTest() {
        userDto.setEmail("zerodha@gmail.com");
        when(userService.updateUser(userDto, 1L)).thenReturn(userDto);
        assertThat(userController.updateUser(userDto, 1L).getBody().getEmail()).isEqualTo("zerodha@gmail.com");
    }

    @Test
    void deleteUserTest() {
        ResponseEntity<ApiResponse> responseEntity = userController.deleteUser(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); // Expect HTTP status OK
        assertEquals("User deleted successfully", responseEntity.getBody().getMessage()); // Expect response body message
        verify(userService).deleteUser(1L);
    }

    @Test
    void getAllUsersTest() {
        UserResponse userResponse = UserResponse.builder().users(List.of(userDto,userDto2)).pageNumber(0).pageSize(2).totalPages(2).totalPages(1).isLastPage(true).build();
        when(userService.getAllUsers(0, 2, "id", "asc")).thenReturn(userResponse);
        assertThat(userController.getAllUsers(0,2,"id","asc").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getSingleUserTest() {
        when(userService.getUserById(1L)).thenReturn(userDto);
        assertThat(userController.getSingleUser(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}