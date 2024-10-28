package com.eshop.serviceuser.repository;

import com.eshop.serviceuser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.online = true WHERE u.username = :username")
    void updateUserOnlineStatus(String username);
}
