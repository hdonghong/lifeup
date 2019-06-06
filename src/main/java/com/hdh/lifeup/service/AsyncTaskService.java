package com.hdh.lifeup.service;

import com.hdh.lifeup.exception.AsyncException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AsyncTaskService class<br/>
 * 异步任务类
 * @author hdonghong
 * @since 2019/06/05
 */
@Component
public class AsyncTaskService {

    @Async("taskExecutor")
    public void doLike() throws AsyncException {
        System.out.println("任务一，当前线程：" + Thread.currentThread().getName());
    }


    @Async("taskExecutor")
    public void doTaskFour(Future<?> future) throws AsyncException {
        try {
            System.err.println("future = " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
