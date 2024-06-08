package com.example.aibouauth.core.user;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class changePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
