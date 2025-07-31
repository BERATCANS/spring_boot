package com.beratcan.first_steps_on_kron.service;


import com.beratcan.first_steps_on_kron.Repository.UserRepository;
import com.beratcan.first_steps_on_kron.exception.UnauthorizedException;
import com.beratcan.first_steps_on_kron.model.Role;
import com.beratcan.first_steps_on_kron.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void register(User user, boolean isAdmin) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(isAdmin ? Role.ROLE_ADMIN : Role.ROLE_USER);
        userRepo.save(user);
    }

    public ResponseEntity<?> login(User loginUser) {
        User user = userRepo.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Wrong password");
        }

        String role = user.getRole().name();

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "role", role
        ));
    }
}

