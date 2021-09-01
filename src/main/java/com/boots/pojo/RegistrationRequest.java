package com.boots.pojo;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegistrationRequest {
    @Schema(description = "username", example = "user")
    private String username;
    @Schema(description = "email", example = "user@gmail.com")
    private String email;
    @Schema(description = "role", example = "[\"user\"]")
    private String role;
    @Schema(description = "password", example = "user")
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
