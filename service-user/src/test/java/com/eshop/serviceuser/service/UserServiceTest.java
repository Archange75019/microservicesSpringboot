package com.eshop.serviceuser.service;

import com.eshop.serviceuser.dto.UserDto;
import com.eshop.serviceuser.model.User;
import com.eshop.serviceuser.repository.UserRepository;
import com.eshop.serviceuser.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static reactor.retry.Repeat.times;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto createUserInvalidCredentialsDto() {
        // Créer et retourner un UserDto avec les attributs nécessaires
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");
        return userDto;
    }
    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe");
        userDto.setPassword("password123");
        userDto.setEmail("johndoe@example.com");
        userDto.setPhone("01.02.03.04.05");
        userDto.setRole("USER");
        userDto.setOnline(false);
        userDto.setIsVerified(false);
        userDto.setNumberStreet(123);
        userDto.setTypeStreet("Avenue");
        userDto.setNameStreet("Liberty");
        userDto.setZipCode("75001");
        userDto.setCity("Paris");
        return userDto;
    }

    private User createUser() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("$2a$10$DowJonesiiLS48K/XZlOP3uEjLSjRy6.sOqfkcF8x4hNk/wrUYzH");
        user.setEmail("johndoe@example.com");
        user.setPhone("01.02.03.04.05");
        user.setRole("USER");
        user.setOnline(false);
        user.setVerified(false);
        user.setNumberStreet(123);
        user.setTypeStreet("Avenue");
        user.setNameStreet("Liberty");
        user.setZipCode("75001");
        user.setCity("Paris");
        return user;
    }


    @Test
    void testRegisterUser_Success() {
        UserDto userDto = createUserDto();

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(createUser());

        String result = userService.registerUser(userDto);

        assertEquals("Inscription réussie", result);
        verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testRegisterUser_UserExistsFailure() {
        UserDto userDto = createUserDto();

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(new User()));

        String result = userService.registerUser(userDto);

        assertEquals("Utilisateur déjà inscrit", result);
        verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    void testRegisterUser_NullUserDtoArgumentFailure() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

//    @Test
//    void testValidateAndGenerateToken_Success() {
//        UserDto userDto = createUserDto();
//        userDto.setUsername("johndoe");
//        userDto.setPassword("password123");
//
//        User user = createUser();
//        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
//        when(jwtUtil.generateToken("johndoe")).thenReturn("dummyToken");
//
//        String token = userService.validateAndGenerateToken(userDto);
//
//        assertNotNull(token);
//        assertFalse(token.isEmpty());
//        String[] parts = token.split("\\.");
//        assertEquals(3, parts.length, "JWT token must consist of three parts separated by dots.");
//
//        verify(userRepository).updateUserOnlineStatus("johndoe");
//    }
//    @Test
//    void testValidateAndGenerateToken_InvalidCredentials() {
//        UserDto userDto = createUserInvalidCredentialsDto();
//
//        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.validateAndGenerateToken(userDto);
//        });
//
//        assertEquals("Invalid credentials", exception.getMessage());
//    }


    @Test
    void testLogoutUserById_Success() {
        User user = createUser();
        user.setId(1L); // Assuming the user ID is 1

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userService.logoutUserById(1L);

        assertTrue(result);
        assertFalse(user.isOnline());
        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testLogoutUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.logoutUserById(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }
}