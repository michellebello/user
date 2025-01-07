package com.example.user.user;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


// where all the CRUD resides
@RestController
@RequestMapping(path="/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // for registering
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        Logger logger = Logger.getLogger(UserController.class.getName());
        try {
            userService.addNewUser(user);
            return ResponseEntity.ok("User has been successfully added");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error registering new user ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not register user " + e.getMessage());
        }
    }

    // for logging in
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Logger logger = Logger.getLogger(UserController.class.getName());

        try {
            // Validate credentials (you'll need to implement this logic in userService)
            boolean isAuthenticated = userService.isAuthenticated(user.getUsername(), user.getPassword());

            if (isAuthenticated) {
                // Optionally, you can return a token here (JWT or session-based token)
                return ResponseEntity.ok("Login successful!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error logging in user ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not log in user: " + e.getMessage());
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping (path= "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path="{userId}")
    public void updateUser(@PathVariable("userId") Long userId,
                           @PathVariable(required = false) String username,
                           @PathVariable(required = false) String email) {
        userService.updateUser(userId, username, email);
    }

}
