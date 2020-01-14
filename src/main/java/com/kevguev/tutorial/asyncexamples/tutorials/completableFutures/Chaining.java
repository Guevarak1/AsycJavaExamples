package com.kevguev.tutorial.asyncexamples.tutorials.completableFutures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Chaining {
    /*
    combining completable futures
    thenCompose() chains previous completableFuture with next completableFuture
    thenCombine() takes previous completableFuture, accepts new CompletableFuture and can pass a function to it to combine
     */
    public static void completableFutureCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello World")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " Composed"));

        CompletableFuture<String> combineFuture = CompletableFuture.supplyAsync(() -> "Hello World")
                .thenCombine(CompletableFuture.supplyAsync(() -> " Combined"), (s, u) -> s + u);

        System.out.println(future.get());
        System.out.println(combineFuture.get());
    }

    /*
   chaining initial completableFuture with .thenApply() -> .thenAccept() -> thenRun()
    */
    public static void completableFutureChaining() throws ExecutionException, InterruptedException {
        CompletableFuture<String> callableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "CompletableFuture that spins off a supplier at: " + System.currentTimeMillis();
        });

        CompletableFuture<String> thenApplyCompletableFuture = callableFuture.thenApply(s -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s + " then apply called at " + +System.currentTimeMillis();
        });

        thenApplyCompletableFuture.thenAccept(s -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(s + " then accept called at: " + System.currentTimeMillis());
        });

        //concluding chain
        thenApplyCompletableFuture.thenRun(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finished operation at: " + +System.currentTimeMillis());
        });

        System.out.println(thenApplyCompletableFuture.get());
    }
}
