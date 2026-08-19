package com.datingapp.backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CreateUserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
