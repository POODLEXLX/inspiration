package org.poodle.nodecore;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NodeCoreProcess {

    private static final NodeCoreProcess INSTANCE = new NodeCoreProcess();

    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static final ThreadPoolExecutor DEFAULT_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS - 2, AVAILABLE_PROCESSORS, 1, TimeUnit.HOURS,
            new ArrayBlockingQueue<>(5000), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 任务队列
     */
    private ConcurrentLinkedQueue<FutureTask> taskQueue = new ConcurrentLinkedQueue<>();

    public void addTask(FutureTask task){
        taskQueue.add(task);
    }

    private NodeCoreProcess(){
        while (true){
            FutureTask task = taskQueue.poll();
            if (task != null){
                Future future = DEFAULT_EXECUTOR.submit(task);
            }
        }
    }

}
