package com.boots.controller;

import com.boots.pojo.RegistrationRequest;
import com.boots.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/registration")
@Tag(name = "Registration controller", description = "Allows you to register users/admins and assign roles")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @Operation(summary = "Registration user controller")
    @PostMapping()
    public ResponseEntity<?> registrationUser(@RequestBody RegistrationRequest registrationRequest) {
        return registrationService.registrationUser(registrationRequest);
    }


}
