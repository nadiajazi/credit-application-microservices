package com.example.aibouauth.core.auth;

import com.example.aibouauth.core.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private  String phone;
    private Role role;
}