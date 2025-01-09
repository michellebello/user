package com.example.user.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public List <User> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(User user){
        Optional <User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("user already exists, email is already taken");
        }
        userRepository.save(user);
    }

    public boolean isAuthenticated(String username, String password) {
        User user = userRepository.findUserByUsername(username).
                orElseThrow(() -> new IllegalStateException("username " + username + " not found in system, try registering first"));
        if (!password.equals(user.getPassword())){
            return false;
        }
        return true;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username); // Calls the repo method
    }


    public void deleteUser(Long userId) {
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new IllegalStateException("user with id " + userId + " does not exists" );
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String username, String email) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(()->
                new IllegalStateException("user with id " + userId + " does not exit"));

        if (username != null && !username.isEmpty() && !Objects.equals(userToUpdate.getUsername(), username)) {
            userToUpdate.setUsername(username);
        }

        if (email!= null && !email.isEmpty() && !Objects.equals(userToUpdate.getEmail(), email)) {
            userToUpdate.setEmail(email);
        }

    }
}
