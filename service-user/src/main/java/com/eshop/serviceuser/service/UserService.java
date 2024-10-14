package com.eshop.serviceuser.service;

import com.eshop.serviceuser.dto.UserDto;
import com.eshop.serviceuser.model.User;
import com.eshop.serviceuser.repository.UserRepository;
import com.eshop.serviceuser.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public String registerUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return "Utilisateur déjà inscrit";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setOnline(userDto.getOnline());
        user.setVerified(userDto.getIsVerified());
        user.setNumberStreet(userDto.getNumberStreet());
        user.setTypeStreet(userDto.getTypeStreet());
        user.setNameStreet(userDto.getNameStreet());
        user.setZipCode(userDto.getZipCode());
        user.setCity(userDto.getCity());
         userRepository.save(user);
        return "Inscription réussie";
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String validateAndGenerateToken(UserDto userDto) {
        if (userDto == null || userDto.getUsername() == null || userDto.getPassword() == null) {
            throw new IllegalArgumentException("User credentials are incomplete");
        }

        Optional<User> userOpt = userRepository.findByUsername(userDto.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean isPasswordValid = passwordEncoder.matches(userDto.getPassword(), user.getPassword());
            if (isPasswordValid) {
                String token = jwtUtil.generateToken(user.getUsername());
                userRepository.updateUserOnlineStatus(user.getUsername());
                return token;
            } else {
                System.out.println("Invalid password attempt for user: " + userDto.getUsername());
            }
        } else {
            System.out.println("User not found: " + userDto.getUsername());
        }
        return null;  // Retourner null en cas d'échec
    }

    @Transactional
    public boolean logoutUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setOnline(false);
            userRepository.save(user);
            System.out.println("User with ID " + userId + " logged out and set to offline.");
            return true;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
