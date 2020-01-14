package com.kevguev.tutorial.asyncexamples.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResource {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
}
