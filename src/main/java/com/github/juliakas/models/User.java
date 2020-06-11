package com.github.juliakas.models;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private Long balance;
}
