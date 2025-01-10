package com.example.user.user.repository;

import java.util.Optional;

import com.example.user.user.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT u FROM User u WHERE u.email=?1")
    Optional <User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username=?1")
    Optional <User> findUserByUsername(String username);
}
