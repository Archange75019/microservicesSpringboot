package com.eshop.serviceuser.service;

import com.eshop.serviceuser.dto.UserDto;
import com.eshop.serviceuser.model.User;
import com.eshop.serviceuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setOnline(userDto.getOnline());
        user.setIsVerified(userDto.getIsVerified());
        user.setNumberStreet(userDto.getNumberStreet());
        user.setTypeStreet(userDto.getTypeStreet());
        user.setNameStreet(userDto.getNameStreet());
        user.setZipCode(userDto.getZipCode());
        user.setCity(userDto.getCity());
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public boolean validateUser(UserDto userDto) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsername(userDto.getUsername()));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPassword().equals(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        return false;
    }

    public boolean logoutUser(String username) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOpt.isPresent()) {
            // Here you might include logic to handle actual logout, like invalidating the session.
            // For now, we'll just print a statement and return true.
            System.out.println("User " + username + " logged out.");
            return true;
        }
        return false;
    }
}
