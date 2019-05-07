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
    public void readCommit() throws InterruptedException {
        System.out.println("readCommit :{}" + "start");
        userService.readCommit();

    }

    @Async
    public void repeatable() throws InterruptedException {
        System.out.println("repeatable :{}" + "start");
        userService.repeatable();
    }

    @Async
    public void readUnCommit() throws InterruptedException {
        System.out.println("readUnCommit :{}" + "start");
        userService.readUnCommit();
    }

    @Async
    public void serializable() throws InterruptedException {
        System.out.println("serializable :{}" + "start");
        userService.serializable();
    }

}
