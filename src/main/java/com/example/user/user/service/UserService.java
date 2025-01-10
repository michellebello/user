package com.example.user.user.service;

import com.example.user.user.repository.UserRepository;
import com.example.user.user.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all users from the repository
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Add a new user
    public void addNewUser(User user) {
        // Check if user already exists by username
        Optional<User> userOptional = userRepository.findUserByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("User already exists, username is already taken");
        }
        // Save the user to the database
        userRepository.save(user);
        System.out.println("User saved: " + user);
    }

    // Authenticate user by checking password
    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void deleteUser(Long userId) {
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String username, String email) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        if (username != null && !username.isEmpty() && !Objects.equals(userToUpdate.getUsername(), username)) {
            userToUpdate.setUsername(username);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(userToUpdate.getEmail(), email)) {
            userToUpdate.setEmail(email);
        }
    }
}
