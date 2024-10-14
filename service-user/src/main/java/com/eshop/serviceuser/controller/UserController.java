package com.eshop.serviceuser.controller;

import com.eshop.serviceuser.dto.UserDto;
import com.eshop.serviceuser.model.User;
import com.eshop.serviceuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody UserDto userDto) {

        return userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDto userDto) {
        boolean isValid = userService.validateUser(userDto);
        if (isValid) {
            // Logic for successful login (e.g., generate JWT token)
            return "Login successful!";
        } else {
            return "Invalid credentials!";
        }
    }

    @PostMapping("/logout")
    public String logoutUser(@RequestBody UserDto userDto) {
        boolean isLoggedOut = userService.logoutUser(userDto.getUsername());
        if (isLoggedOut) {
            return "Logout successful!";
        } else {
            return "Logout failed!";
        }
    }

    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {

        return userService.findByUsername(username);
    }
}
