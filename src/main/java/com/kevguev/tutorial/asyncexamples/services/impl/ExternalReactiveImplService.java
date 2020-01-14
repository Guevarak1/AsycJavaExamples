package com.kevguev.tutorial.asyncexamples.services.impl;

import com.kevguev.tutorial.asyncexamples.models.PostResource;
import com.kevguev.tutorial.asyncexamples.models.UserResource;
import com.kevguev.tutorial.asyncexamples.services.ExternalReactiveService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ExternalReactiveImplService implements ExternalReactiveService {
    @Override
    public Flux<PostResource> retrievePostsForUser(Long userId) {
        Flux<PostResource> postResourceFlux = WebClient.create()
                .get()
                .uri("https://jsonplaceholder.typicode.com/users")
                .retrieve()
                .bodyToFlux(PostResource.class);

        System.out.println("Exiting NON-BLOCKING  external service call!");
        return postResourceFlux;
    }

    @Override
    public Flux<UserResource> retrieveAllUserIds() {
        System.out.println("Starting NON-BLOCKING external service call!");
        Flux<UserResource> userResourceFlux = WebClient.create()
                .get()
                .uri("https://jsonplaceholder.typicode.com/users")
                .retrieve()
                .bodyToFlux(UserResource.class);

        System.out.println("Exiting NON-BLOCKING  external service call!");
        return userResourceFlux;
    }
}
