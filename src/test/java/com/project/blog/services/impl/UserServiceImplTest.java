package com.project.blog.services.impl;

import com.project.blog.dtos.RoleDto;
import com.project.blog.dtos.UserDto;
import com.project.blog.dtos.UserDtoResponse;
import com.project.blog.dtos.UserResponse;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.project.blog.entities.Role;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.exceptions.ValidationException;
import com.project.blog.repositories.RoleRepo;
import com.project.blog.repositories.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import org.springframework.data.domain.Page;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    User user;
    Role userRole;
    Role adminRole;
    RoleDto userRoleDto;
    RoleDto adminRoleDto;
    UserDto userDto;
    UserDto updatedUserDto;
    UserDto userDto2;
    UserDtoResponse userDtoResponse;
    List<UserDto> users;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mock(RoleRepo.class);
        mock(UserRepo.class);
        mock(ModelMapper.class);
        mock(PasswordEncoder.class);

        userRole = Role.builder().id(1).role("ROLE_USER").build();
        adminRole = Role.builder().id(2).role("ROLE_ADMIN").build();
        userRoleDto = RoleDto.builder().id(1).role("ROLE_USER").build();
        adminRoleDto = RoleDto.builder().id(2).role("ROLE_ADMIN").build();
        user = User.builder().id(1L).name("abc").email("broker@gmail.com").password("$2a$12$pNI/S5F6uJ19JSrssJYBOeKFEIPxCUT8CVsrF4cxuEPspTlNwiI5C").about("co-founder and CEO of an online stock brokerage").roles(new HashSet<>(List.of(userRole))).build();
        userDto = UserDto.builder().id(1L).name("abc").email("broker@gmail.com").password("Aa1@6578").about("co-founder and CEO of an online stock brokerage").build();
        userDto2 = UserDto.builder().id(2L).name("Amitabh").password("Sh@hen5hah").about("I am Amitabh Bachchan an Indian film actor.").build();
        updatedUserDto = UserDto.builder().id(1L).name("abc").email("broker@gmail.com").password("Aa1@6578").about("I'm co-founder and CEO of an online stock brokerage.").build();
        userDtoResponse = UserDtoResponse.builder().id(1L).name("abc").email("broker@gmail.com").about("co-founder and CEO of an online stock brokerage").build();
        users = new ArrayList<>();
        users.add(userDto);
        users.add(userDto2);
        //userResponse = UserResponse.builder().users(users).pageNumber(0).pageSize(10).totalElements(2L).totalPages(1).isLastPage(true).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createUserTest() {
        when(userRepo.findByEmail("broker@gmail.com")).thenReturn(null);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(user.getPassword());
        when(roleRepo.findByRole("ROLE_USER")).thenReturn(userRole);
        when(userRepo.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDtoResponse.class)).thenReturn(userDtoResponse);
        assertThat(userService.createUser(userDto).getName()).isEqualTo("abc");
    }

    @Test
    void createAlreadyExistingUserTest() {
        when(userRepo.findByEmail("broker@gmail.com")).thenReturn(user);
        assertThrows(ValidationException.class, () -> userService.createUser(userDto));
    }

    @Test
    void updateUserTest() {
        Set<RoleDto> roles = new HashSet<>();
        roles.add(userRoleDto);
        updatedUserDto.setRoles(roles);
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(roleRepo.findByRole("ROLE_USER")).thenReturn(userRole);
        when(userRepo.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(updatedUserDto);
        assertThat(userService.updateUser(updatedUserDto, 1L).getAbout()).isEqualTo("I'm co-founder and CEO of an online stock brokerage.");
    }

    @Test
    void updateNotExistingUserTest() {
        when(userRepo.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(updatedUserDto, 100L));
    }

    @Test
    void updateUserWihRoleAdmin() {
        Set<RoleDto> roles = new HashSet<>();
        roles.add(userRoleDto);
        roles.add(adminRoleDto);
        updatedUserDto.setRoles(roles);
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(roleRepo.findByRole("ROLE_USER")).thenReturn(userRole);
        when(roleRepo.findByRole("ROLE_ADMIN")).thenReturn(adminRole);
        when(userRepo.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(updatedUserDto);
        //assertThat(userService.updateUser(updatedUser, 1L).getRoles().size()).isEqualTo(2);
        assertThat(userService.updateUser(updatedUserDto, 1L).getRoles().stream().filter(role -> role.getRole().equals("ROLE_ADMIN")).count()).isEqualTo(1);
    }

    @Test
    void getUserByIdTest() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        assertThat(userService.getUserById(1L).getEmail()).isEqualTo("broker@gmail.com");
    }

    @Test
    void getUserByIdNotFound() {
        when(userRepo.findById(1000L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1000L));
    }

    @Test
    public void testGetAllUsers() {
        User user2 = User.builder().id(2L).name("Amitabh").password("Sh@hen5hah").about("I am Amitabh Bachchan an Indian film actor.").roles(new HashSet<>(List.of(userRole))).build();
        List<User> userList = new ArrayList<>(List.of(user, user2));
        Page<User> pageUser = new PageImpl<>(userList);
        Mockito.when(userRepo.findAll(any(Pageable.class))).thenReturn(pageUser);
        Mockito.when(modelMapper.map(any(User.class), eq(UserDto.class))).thenAnswer(invocation -> {
            User userd = invocation.getArgument(0);
            return UserDto.builder().id(userd.getId()).name(userd.getName()).password(userd.getPassword()).email(userd.getEmail()).about(userd.getPassword()).build();
        });
        UserResponse userResponse = userService.getAllUsers(0, 2, "id", "asc");
        assertEquals(2, userResponse.getUsers().size()); // Ensure correct number of users
        assertEquals(0, userResponse.getPageNumber()); // Ensure correct page number
        assertEquals(2, userResponse.getPageSize()); // Ensure correct page size
        assertEquals(1, userResponse.getTotalPages()); // Ensure correct total pages
        assertEquals(2, userResponse.getTotalElements()); // Ensure correct total elements
        assertTrue(userResponse.getIsLastPage()); // Ensure it's not the last page
    }

    @Test
    public void deleteUserTest() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        userService.deleteUser(1L);
        Mockito.verify(userRepo, times(1)).delete(user);
    }

    @Test
    public void deleteUserNotFoundTest() {
        when(userRepo.findById(1000L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1000L));
    }
}