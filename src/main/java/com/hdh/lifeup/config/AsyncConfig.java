package com.hdh.lifeup.config;

import com.hdh.lifeup.exception.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * AsyncConfig class<br/>
 * refer to
 * https://juejin.im/post/5b27b8366fb9a00e46675879
 * https://juejin.im/post/5b1e36d8f265da6e1c4ae720
 * https://www.cnblogs.com/waytobestcoder/p/5323130.html
 * https://github.com/middleware-tech/blog/wiki/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%A4%A7%E5%B0%8F%E8%AE%BE%E7%BD%AE
 * @author hdonghong
 * @since 2019/06/05
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 连接池配置依据：
     * 任务的性质：CPU密集型任务、IO密集型任务、混合型任务
     * 任务的优先级：高、中、低
     * 任务的执行时间：长、中、短
     * 任务的依赖性：是否依赖其他系统资源，如数据库连接等
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // tasks*taskTime = (100 ~ 200) * 0.1
        executor.setCorePoolSize(10);
        // (max(tasks)- queueCapacity)/(1/taskTime) = (200 - 100) / (1/0.1)
        executor.setMaxPoolSize(20);
        // (coreSizePool/taskTime)*responseTime = 10 / 0.1 * 1
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 优雅关闭连接池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 如果不初始化，导致找到不到执行器
        executor.initialize();
        return executor;
    }

    /**
     * 异常处理
     * Spring对于2种异步方法的异常处理机制如下：
     * 对于方法返回值是Futrue的异步方法: a) 在调用future的get时捕获异常; b) 在异常方法中直接捕获异常
     * 对于返回值是void的异步方法：通过AsyncUncaughtExceptionHandler处理异常
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

}
