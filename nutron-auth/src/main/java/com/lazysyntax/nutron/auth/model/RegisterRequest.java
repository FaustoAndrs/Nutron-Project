package com.lazysyntax.nutron.auth.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String fullname;
    private String email;
    private String password;

}
