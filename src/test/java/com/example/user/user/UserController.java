package com.example.user.user;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


// where all the CRUD resides
@RestController
@RequestMapping(path="/users")
public class UserController {
    private final UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

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
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
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
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            User user = userService.findUserByUsername(userDto.getUsername())
                    .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
            if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
                throw new IllegalStateException("Password does not match, try again.");
            }
            return ResponseEntity.ok("Successfully logged in");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
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
