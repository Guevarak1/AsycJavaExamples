package com.kevguev.tutorial.asyncexamples.services.impl;

import com.kevguev.tutorial.asyncexamples.models.PostResource;
import com.kevguev.tutorial.asyncexamples.models.UserResource;
import com.kevguev.tutorial.asyncexamples.services.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalServiceImpl implements ExternalService {
    private RestTemplate restTemplate;

    @Autowired
    public ExternalServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<PostResource> retrievePostsForUser(Long userId) {
        String postUri = "https://jsonplaceholder.typicode.com/posts?userId={userId}";
        //blocking, will not advance until after complete
        ResponseEntity<PostResource[]> postsResponseEntity = restTemplate.getForEntity(postUri,
                PostResource[].class,
                userId);

        PostResource[] posts = postsResponseEntity.getBody();
        return Arrays.asList(posts);
    }

    @Override
    public List<Long> retrieveAllUserIds() {
        String usersUri = "https://jsonplaceholder.typicode.com/users";
        ResponseEntity<List<UserResource>> allUsersResponseEntity = restTemplate.exchange(usersUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserResource>>() {
                });

        List<UserResource> users = allUsersResponseEntity.getBody();
        if (users == null) System.out.println("error retrieving users");

        return users.stream().map(UserResource::getId).collect(Collectors.toList());

    }
}
