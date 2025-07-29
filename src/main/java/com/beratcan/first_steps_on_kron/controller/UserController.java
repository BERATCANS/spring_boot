package com.beratcan.first_steps_on_kron.controller;

import com.beratcan.first_steps_on_kron.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beratcan.first_steps_on_kron.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user,@RequestParam(required = false, defaultValue = "false") boolean isAdmin) {
        userService.register(user, isAdmin);
        return ResponseEntity.ok("User registered successfully");
    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(@RequestParam String username) {
        User user = (User) userService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        return userService.login(loginUser);
    }
}

