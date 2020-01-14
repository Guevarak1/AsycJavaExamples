package com.kevguev.tutorial.asyncexamples.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tweet {
    private String tweet;
    private String user;

    public Tweet(String tweet, String user) {
        this.tweet = tweet;
        this.user = user;
    }
}