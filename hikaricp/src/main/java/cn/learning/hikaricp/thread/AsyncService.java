package cn.learning.hikaricp.thread;

import cn.learning.hikaricp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserThread1
 * @Description 读取提交的数据
 * @Author clare
 * @Date 2019/5/6 23:55
 * @Version 1.0
 */
@Service
public class AsyncService {

    @Autowired
    private UserService userService;

    @Async
    public void executeAsync1() throws InterruptedException {
        System.out.println("异步任务::1");
        userService.readCommit();

    }

    @Async
    public void executeAsync2() throws InterruptedException {
        System.out.println("异步任务::2");
        System.out.println("异步任务::1");
        userService.readCommit();
    }

}
