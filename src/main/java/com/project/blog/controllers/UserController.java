package com.project.blog.controllers;

import com.project.blog.dtos.ApiResponse;
import com.project.blog.dtos.UserDto;
import com.project.blog.dtos.UserDtoResponse;
import com.project.blog.dtos.UserResponse;
import com.project.blog.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Tag(name = "UserController", description = "APIs for users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    //POST - Create a User
    @PostMapping("/create")
    public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDto userDto) {
        UserDtoResponse createdUserDtoResponse =  this.userService.createUser(userDto);
        return new ResponseEntity<>(createdUserDtoResponse, HttpStatus.CREATED);
    }

    //PUT - Update a User
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable Long userId) {
        UserDto updatedUser = this.userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //Delete - Delete a User
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("User deleted successfully", true), HttpStatus.OK);
    }

    //Get - Get all Users
    @GetMapping("/get")
    public ResponseEntity<UserResponse> getAllUsers(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                                     @RequestParam(value =  "sortBy", required = false, defaultValue =  "id") String sortBy,
                                                     @RequestParam(value =  "sortDir", required = false, defaultValue =  "asc") String sortDir) {
        log.info("REQUEST ARRIVED TO GET ALL USERS");
        return new ResponseEntity<>(this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //Get - get a user by id
    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Long userId) {
        log.info("REQUEST ARRIVED TO GET USER WITH ID {}", userId);
        return  new ResponseEntity<>(this.userService.getUserById(userId), HttpStatus.OK);
    }
}
