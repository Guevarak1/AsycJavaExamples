package com.kevguev.tutorial.asyncexamples.controllers;

import com.kevguev.tutorial.asyncexamples.models.PostResource;
import com.kevguev.tutorial.asyncexamples.models.UserResource;
import com.kevguev.tutorial.asyncexamples.services.ExternalReactiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/*
Spring 5 with WebClient method for calling external web services
 */
@RestController
@RequestMapping(value = "/postsV2")
public class PostsReactiveController {

    private final ExternalReactiveService externalReactiveService;

    public PostsReactiveController(ExternalReactiveService externalReactiveService) {
        this.externalReactiveService = externalReactiveService;
    }

    @GetMapping("/allUsersPosts")
    public ResponseEntity<List<PostResource>> retrieveAllPostsForAllUsers() {
        //first we will retrieve all users to get their ids
        List<UserResource> userResources = externalReactiveService.retrieveAllUserIds()
                .collect(Collectors.toList())
                .block();

        List<Long> userIds = userResources.stream()
                .map(UserResource::getId)
                .collect(Collectors.toList());

        //TODO retrieve all users posts async with externalReactiveService.retrievePostsForUser

        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
}
