package cn.learning.hikaricp.thread;

import cn.learning.hikaricp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserThread
 * @Description 读取未提交的数据
 * @Author clare
 * @Date 2019/5/6 23:36
 * @Version 1.0
 */
@Service
public class UserThread implements Runnable {

    @Autowired
    private UserService userService;

    @Override
    public void run() {
        try {
            userService.readUnCommit(13);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
