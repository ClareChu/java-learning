package cn.learning.hikaricp.thread;

import cn.learning.hikaricp.service.UserService;
import cn.learning.hikaricp.service.UserThread1;
import cn.learning.hikaricp.service.UserThread2;
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

    @Autowired
    private UserThread1 userThread1;

    @Autowired
    private UserThread2 userThread2;

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

    @Async
    public void read1(int id, Object o) throws InterruptedException {
        System.out.println("read1 :{}" + "start");
        userThread1.read(id, o);
    }

    @Async
    public void read2(int id, Object o) throws InterruptedException {
        System.out.println("read2 :{}" + "start");
        userThread2.read(id, o);
    }


    @Async
    public void read3(int id) throws InterruptedException {
        System.out.println("read1 :{}" + "start");
        userThread1.read1(id);
    }

    @Async
    public void read4(int id) throws InterruptedException {
        System.out.println("read2 :{}" + "start");
        userThread2.read1(id);
    }
}
