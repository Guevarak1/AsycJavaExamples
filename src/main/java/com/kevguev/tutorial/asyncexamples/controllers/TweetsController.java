package com.kevguev.tutorial.asyncexamples.controllers;

import com.kevguev.tutorial.asyncexamples.models.Tweet;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping(value = "/tweets")
public class TweetsController {

    private final RestTemplate restTemplate;

    public TweetsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/slow-service-tweets")
    private List<Tweet> getAllTweets() throws InterruptedException {
        Thread.sleep(2000L); // delay
        return Arrays.asList(
                new Tweet("RestTemplate for Spring 4", "@user1"),
                new Tweet("WebClient for Spring 5", "@user2"),
                new Tweet("OK, both are useful", "@user1"));
    }

    @GetMapping("/tweets-blocking")
    public List<Tweet> getTweetsBlocking() {
        final String uri = "http://localhost:8080/tweets/slow-service-tweets";

        System.out.println("Entering blocking controller");
        ResponseEntity<List<Tweet>> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Tweet>>() {
                });


        List<Tweet> result = response.getBody();
        result.forEach(tweet -> System.out.println(tweet.toString()));
        System.out.println("Exiting BLOCKING Controller");
        return result;
    }

    /*
    Spring 5 way of making an async call
     */
    @GetMapping(value = "/tweets-non-blocking",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> getTweetsNonBlocking() {
        System.out.println("Starting NON-BLOCKING Controller!");
        Flux<Tweet> tweetFlux = WebClient.create()
                .get()
                .uri("http://localhost:8080/tweets/slow-service-tweets")
                .retrieve()
                .bodyToFlux(Tweet.class);

        tweetFlux.subscribe(tweet -> System.out.println(tweet.toString()));
        System.out.println("Exiting NON-BLOCKING Controller!");
        return tweetFlux;
    }
}
