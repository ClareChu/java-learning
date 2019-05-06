package cn.learning.hikaricp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @ClassName AsyncConfig
 * @Description TODO
 * @Author clare
 * @Date 2019/5/7 00:11
 * @Version 1.0
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        taskExecutor.setCorePoolSize(8);
        //最大线程数
        taskExecutor.setMaxPoolSize(16);
        //队列大小
        taskExecutor.setQueueCapacity(100);
        taskExecutor.initialize();
        return taskExecutor;
    }
}