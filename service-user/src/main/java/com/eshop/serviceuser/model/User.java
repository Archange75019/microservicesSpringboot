package com.eshop.serviceuser.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String online;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private String isVerified;

    @Column(nullable = false)
    private int numberStreet;

    @Column(nullable = false)
    private String TypeStreet;

    @Column(nullable = false)
    private String nameStreet;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String city;

}
