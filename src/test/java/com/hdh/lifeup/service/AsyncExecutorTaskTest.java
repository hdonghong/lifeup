package com.hdh.lifeup.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncExecutorTaskTest {

    @Autowired
    private AsyncExecutorTask asyncExecutorTask;

    @Test
    public void testAsyncExecutorTask() throws Exception {
        asyncExecutorTask.doTaskOne();
        asyncExecutorTask.doTaskTwo();
        asyncExecutorTask.doTaskThree();
    }

}