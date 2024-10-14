package com.eshop.serviceuser.controller;

import com.eshop.serviceuser.dto.UserDto;
import com.eshop.serviceuser.model.User;
import com.eshop.serviceuser.service.UserService;
import com.eshop.serviceuser.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;


    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Bad Request if the header is missing or incorrectly formatted
        }

        String token = authorizationHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            boolean isValid = jwtUtil.validateToken(token, username);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        return userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody UserDto userDto) {
        String token = userService.validateAndGenerateToken(userDto);
        Map<String, Object> response = new HashMap<>();

        if (token != null) {
            // On suppose que le UserService renvoie les informations utilisateur en cas de succès
            Optional<User> userOpt = userService.findByUsername(userDto.getUsername());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                response.put("token", token);
                response.put("id", user.getId());
                response.put("username", user.getUsername());
                response.put("message", "Login successful!");
                System.out.println("Token generated: " + token);
            } else {
                response.put("message", "User not found!");
            }
        } else {
            response.put("message", "Invalid credentials!");
        }

        return response;
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<String> logoutUserById(@PathVariable Long id) {
        try {
            boolean isLoggedOut = userService.logoutUserById(id);
            if (isLoggedOut) {
                return ResponseEntity.ok("Logout successful!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed!");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {

        return userService.findByUsername(username);
    }
}
