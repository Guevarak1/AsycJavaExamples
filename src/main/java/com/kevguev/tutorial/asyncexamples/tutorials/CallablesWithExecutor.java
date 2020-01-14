package com.kevguev.tutorial.asyncexamples.tutorials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallablesWithExecutor {
    /* https://www.baeldung.com/java-executor-service-tutorial
    ExecutorService examples with callable tasks
    */
    private static void submitCallablesToExecutor_InvokeAll_RetrieveResults() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Runnable runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.println("Runnable Task executed at: " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            return "Callable Task's execution at: " + System.currentTimeMillis();
        };

        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);

        //Runnable task that does not return anything
        executorService.execute(runnableTask);

        //single callable task that returns string with timestamp
        Future<String> future = executorService.submit(callableTasks.get(0));
        final String str = future.get();
        System.out.println(str);

        //list of callable tasks returned as futures with timestamps
        List<Future<String>> futures = executorService.invokeAll(callableTasks);
        futures.forEach(stringFuture -> {
            try {
                System.out.println(stringFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }
}
