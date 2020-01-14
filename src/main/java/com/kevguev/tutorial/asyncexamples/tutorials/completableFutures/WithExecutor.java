package com.kevguev.tutorial.asyncexamples.tutorials.completableFutures;

import java.util.concurrent.*;

public class WithExecutor {
    /*
   https://www.baeldung.com/java-completablefuture
   send of a completableFuture that returns a string print times.
   create CF instance,
   set the executorService,
   submit runnable which sets CF's result,
   return result
    */
    private static void completableFutureAsSimpleFuture() throws ExecutionException, InterruptedException {
        System.out.println("Hello pre completable Future at: " + System.currentTimeMillis());
        final Future<String> stringFuture = calculateAsync();

        //blocking call that wont go to the next line until .get is finished
        //if cancelled, catch Cancellation Exception and handle error
        //try {
        //    final String stringResult = stringFuture.get();
        //} catch (CancellationException e) {
        //    System.out.println("future cancelled: " + e.getMessage());
        //}

        final String stringResult = stringFuture.get();
        System.out.println(stringResult);
        System.out.println("Hello post completable Future at: " + System.currentTimeMillis());
    }

    //asynchronous code with boiler plate
    public static Future<String> calculateAsyncCancel() {
        //create completable Future instance
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        //spin off computation in another thread
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                Thread.sleep(500);
                //if cancelled, we throw an exception, catch Cancellation Exception
                completableFuture.cancel(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();

        //immediately return completableFuture, returns before .sleep finishes.
        return completableFuture;

    }

    //asynchronous code with boilerplate
    public static Future<String> calculateAsync() {
        //create completable Future instance
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        //spin off computation in another thread
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //when sleep is done, set completableFutures result
            completableFuture.complete("Hello completableFuture post sleep(500) at: " + System.currentTimeMillis());
        });

        executorService.shutdown();
        //immediately return completableFuture, returns before .sleep finishes.
        return completableFuture;
    }
}
