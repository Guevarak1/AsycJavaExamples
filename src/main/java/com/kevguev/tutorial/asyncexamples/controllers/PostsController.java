package com.kevguev.tutorial.asyncexamples.controllers;

import com.kevguev.tutorial.asyncexamples.models.PostResource;
import com.kevguev.tutorial.asyncexamples.services.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/*
Spring 4 with RestTemplate method for calling external web services
 */
@RestController
@RequestMapping(value = "/posts")
public class PostsController {

    private ExternalService externalService;

    @Autowired
    public PostsController(ExternalService externalService) {
        this.externalService = externalService;
    }

    /*
    async call takes 500ms.
     */
    @GetMapping("/allUsersPostsAsync")
    public ResponseEntity<List<PostResource>> retrieveAllPostsForAllUsersAsync() {
        //first we will retrieve all users to get their ids
        List<Long> userIds = externalService.retrieveAllUserIds();

        //then we will search for every post based on userId
        List<CompletableFuture<List<PostResource>>> postResourcesAsync = new ArrayList<>();
        for (Long userId : userIds) {
            CompletableFuture<List<PostResource>> postResourceCompleteFuture = CompletableFuture.supplyAsync(() -> externalService.retrievePostsForUser(userId));
            postResourcesAsync.add(postResourceCompleteFuture);
        }

        List<PostResource> postResources = postResourcesAsync.stream()
                .map(CompletableFuture::join)//returns List<List<PostResource>>
                .flatMap(List::stream) //flatten list List<PostResource>
                .collect(Collectors.toList());

        return new ResponseEntity<>(postResources, HttpStatus.ACCEPTED);
    }

    /*
    synchronous blocking call takes 1.5 seconds.
     */
    @GetMapping("/allUsersPosts")
    public ResponseEntity<List<PostResource>> retrieveAllPostsForAllUsers() {
        //first we will retrieve all users to get their ids
        List<Long> userIds = externalService.retrieveAllUserIds();

        //then we will search for every post based on userId
        List<PostResource> postResources = new ArrayList<>();
        for (Long userId : userIds) {
            postResources.addAll(externalService.retrievePostsForUser(userId));
        }

        return new ResponseEntity<>(postResources, HttpStatus.ACCEPTED);
    }
}
