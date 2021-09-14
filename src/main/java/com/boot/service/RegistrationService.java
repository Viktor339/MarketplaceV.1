package com.boot.service;


import com.boot.entity.User;
import com.boot.pojo.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  RoleService roleService;

    @Autowired
    private UserService userService;



    public ResponseEntity<?> registrationUser(RegistrationRequest registrationRequest) {


        User user = new User.Builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(roleService.getRole(registrationRequest))
                .build();

        return ResponseEntity.ok(userService.save(user));
    }
}
