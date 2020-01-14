package com.kevguev.tutorial.asyncexamples.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostResource {
    private String userId;
    private String id;
    private String title;
    private String body;
}
