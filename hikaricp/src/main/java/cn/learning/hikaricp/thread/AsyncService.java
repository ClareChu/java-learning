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
    public void readCommit(int id) throws InterruptedException {
        System.out.println("readCommit :{}" + "start");
        userService.readCommit(id);

    }

    @Async
    public void repeatable(int id) throws InterruptedException {
        System.out.println("repeatable :{}" + "start");
        userService.repeatable(id);
    }

    @Async
    public void readUnCommit(int id) throws InterruptedException {
        System.out.println("readUnCommit :{}" + "start");
        userService.readUnCommit(id);
    }

    @Async
    public void serializable(int id) throws InterruptedException {
        System.out.println("serializable :{}" + "start");
        userService.serializable(id);
    }

}
