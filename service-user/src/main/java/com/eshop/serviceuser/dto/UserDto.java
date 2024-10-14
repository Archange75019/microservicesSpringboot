package com.eshop.serviceuser.dto;

import lombok.Data;

@Data
public class UserDto {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private Boolean online;
    private Boolean isVerified;
    private int numberStreet;
    private String typeStreet;
    private String nameStreet;
    private String zipCode;
    private String city;
}
