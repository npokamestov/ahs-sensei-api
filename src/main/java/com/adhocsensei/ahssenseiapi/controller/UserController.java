package com.adhocsensei.ahssenseiapi.controller;

import com.adhocsensei.ahssenseiapi.dao.UserRepository;
import com.adhocsensei.ahssenseiapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserById(@PathVariable Long id) {
        return userRepo.findById(id);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userRepo.save(user);
    }

    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            user.setUserId(id);
            userRepo.save(user);
        }
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userRepo.deleteById(id);
    }

    @GetMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginUser(@RequestBody User user) {
        Optional<User> optionalUser = Optional.ofNullable(userRepo.findByEmail(user.getEmail()));

        if (optionalUser.isPresent()) {
            userRepo.findByEmail(user.getEmail());

            if (user.getPassword().equals(userRepo.findByEmail(user.getEmail()).getPassword())) {
                return userRepo.findByEmail(user.getEmail()).toString();
            } return "Incorrect username or password";

        } return "Incorrect username or password";
    }

}
