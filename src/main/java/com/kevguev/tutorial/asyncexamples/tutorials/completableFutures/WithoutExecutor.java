package com.kevguev.tutorial.asyncexamples.tutorials.completableFutures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WithoutExecutor {
    /*
   Utilizing Completable futures without boilerplate. just .runAsync or .supplyAsync
   hows the executor service handle these calls? uses ForkJoinPool.commonPool()
   https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
    */
    public static void completableFutureWithEncapsulatedComputationLogic() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> runnableFuture = CompletableFuture.runAsync(() -> {
            //long running task
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("CompletableFuture that spins off a runnable at: " + System.currentTimeMillis());
        });
        runnableFuture.get();

        CompletableFuture<String> callableFuture = CompletableFuture.supplyAsync(() -> {
            //long running operation
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "CompletableFuture that spins off a callable at: " + System.currentTimeMillis();
        });
        System.out.println(callableFuture.get());
    }
}
