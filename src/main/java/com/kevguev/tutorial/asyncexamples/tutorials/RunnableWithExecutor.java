package com.kevguev.tutorial.asyncexamples.tutorials;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RunnableWithExecutor {
    private static void spinThreadWithExecutorService() {
        System.out.println("pre hello world at: " + System.currentTimeMillis());

        Thread thread = new Thread(() -> System.out.println("Hello world at: " + System.currentTimeMillis()));
        thread.start();

        System.out.println("post hello world at: " + System.currentTimeMillis());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> System.out.println("hello world from executor at: " + System.currentTimeMillis()));

    }
}
