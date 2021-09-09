package com.boot.service;


import com.boot.entity.Role;
import com.boot.entity.User;
import com.boot.pojo.MessageResponse;
import com.boot.pojo.RegistrationRequest;
import com.boot.repository.RoleRepository;
import com.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> registrationUser(RegistrationRequest registrationRequest) {

        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is exist"));
        }

        Set<Role> roles = new HashSet<>();

        switch (registrationRequest.getRole()) {
            case "admin":
                Role adminRole = roleRepository
                        .findByName(Role.Name.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                roles.add(adminRole);
                break;

            default:
                Role userRole = roleRepository
                        .findByName(Role.Name.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                roles.add(userRole);
        }

        User user = new User.Builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(roles)
                .build();

        return ResponseEntity.ok(userRepository.save(user));

    }
}
