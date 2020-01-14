package com.kevguev.tutorial.asyncexamples.controllers;

import com.kevguev.tutorial.asyncexamples.models.PostResource;
import com.kevguev.tutorial.asyncexamples.models.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/posts")
public class PostsController {

    private RestTemplate restTemplate;

    @Autowired
    public PostsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*
    async call takes 500ms. Spring 4 version of doing async operations
     */
    @GetMapping("/allUsersPostsAsync")
    public ResponseEntity<List<PostResource>> retrieveAllPostsForAllUsersAsync() {
        //first we will retrieve all users to get their ids
        List<Long> userIds = retrieveAllUserIds();

        //then we will search for every post based on userId
        List<CompletableFuture<List<PostResource>>> postResourcesAsync = new ArrayList<>();
        for (Long userId: userIds) {
            CompletableFuture<List<PostResource>> postResourceCompleteFuture = CompletableFuture.supplyAsync(() -> retrievePostsForUser(userId));
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
        List<Long> userIds = retrieveAllUserIds();

        //then we will search for every post based on userId
        List<PostResource> postResources = new ArrayList<>();
        for (Long userId: userIds) {
            postResources.addAll(retrievePostsForUser(userId));
        }

        return new ResponseEntity<>(postResources, HttpStatus.ACCEPTED);
    }

    private List<PostResource> retrievePostsForUser(Long userId) {
        String postUri = "https://jsonplaceholder.typicode.com/posts?userId={userId}";
        //blocking, will not advance until after complete
        ResponseEntity<PostResource[]> postsResponseEntity = restTemplate.getForEntity(postUri,
                PostResource[].class,
                userId);

        PostResource[] posts = postsResponseEntity.getBody();
        return Arrays.asList(posts);
    }

    private List<Long> retrieveAllUserIds() {
        String usersUri = "https://jsonplaceholder.typicode.com/users";
        ResponseEntity<List<UserResource>> allUsersResponseEntity = restTemplate.exchange(usersUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserResource>>() {});

        List<UserResource> users = allUsersResponseEntity.getBody();
        if (users == null) System.out.println("error retrieving users");

        return users.stream().map(UserResource::getId).collect(Collectors.toList());
    }
}
