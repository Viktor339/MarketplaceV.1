package com.boot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name="Test controller", description="Allows test access to users/admins")
public class TestController {

    @Operation(summary="Access controller for everyone", description="Allows access to users who have role user and admin")
    @GetMapping("/all")
    public String allAccess() {
        return "public API";
    }

    @Operation(summary="Access controller for user and admin", description="Allows access to users who have role user and admin")
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "user API";
    }

    @Operation(summary="Access controller for admin", description="Allows access to users who have role admin")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "admin API";
    }
}