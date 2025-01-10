package com.example.user.user.controller;

import com.example.user.user.service.UserService;
import com.example.user.user.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        try {
            System.out.println("Received user registration request: " + user);

            // Hash the password
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            // Call the service to add the user
            userService.addNewUser(user);

            return ResponseEntity.ok("User has been successfully added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not register user: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // Print debug information before calling the service
        System.out.println("Received login attempt: username = " + username + ", password = " + password);

        boolean isAuthenticated = userService.authenticateUser(username, password);

        if (isAuthenticated) {
            return "Login successful!";
        } else {
            return "Invalid username or password";
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path = "{userId}")
    public void updateUser(@PathVariable("userId") Long userId,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String email) {
        userService.updateUser(userId, username, email);
    }
}
