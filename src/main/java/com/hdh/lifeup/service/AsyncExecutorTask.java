package com.hdh.lifeup.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * AsyncExecutorTask class<br/>
 * 异步任务类
 * @author hdonghong
 * @since 2019/06/05
 */
@Component
public class AsyncExecutorTask {

    @Async("taskExecutor")
    public void doTaskOne() throws Exception {
        System.out.println("开始任务一");
        Thread.sleep(3000L);
        System.out.println("任务一，当前线程：" + Thread.currentThread().getName());
    }

    @Async("taskExecutor")
    public void doTaskTwo() throws Exception {
        System.out.println("开始任务二");
        Thread.sleep(1000L);
        System.out.println("任务二，当前线程：" + Thread.currentThread().getName());
    }

    @Async("taskExecutor")
    public void doTaskThree() throws Exception {
        System.out.println("开始任务三");
        Thread.sleep(2500L);
        System.out.println("任务三，当前线程：" + Thread.currentThread().getName());
    }

}
