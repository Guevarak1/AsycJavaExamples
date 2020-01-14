package com.kevguev.tutorial.asyncexamples.services;

import com.kevguev.tutorial.asyncexamples.models.PostResource;

import java.util.List;

public interface ExternalService {
    List<PostResource> retrievePostsForUser(Long userId);
    List<Long> retrieveAllUserIds();
}
