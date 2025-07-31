package com.beratcan.first_steps_on_kron.controller;

import com.beratcan.first_steps_on_kron.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import com.beratcan.first_steps_on_kron.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user,@RequestParam(required = false, defaultValue = "false") boolean isAdmin) {
        userService.register(user, isAdmin);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(@RequestParam String username) {
        User user = (User) userService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            // Create authentication token
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    );

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Create and save context
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // This handles session creation and all the details
            securityContextRepository.saveContext(context, request, response);

            return userService.login(user);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        securityContextRepository.saveContext(emptyContext, request, response);

        return ResponseEntity.ok("Logout successful");
    }
}
