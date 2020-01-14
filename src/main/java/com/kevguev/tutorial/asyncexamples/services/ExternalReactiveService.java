package com.kevguev.tutorial.asyncexamples.services;

import com.kevguev.tutorial.asyncexamples.models.PostResource;
import com.kevguev.tutorial.asyncexamples.models.UserResource;
import reactor.core.publisher.Flux;

public interface ExternalReactiveService {
    Flux<PostResource> retrievePostsForUser(Long userId);

    Flux<UserResource> retrieveAllUserIds();
}
