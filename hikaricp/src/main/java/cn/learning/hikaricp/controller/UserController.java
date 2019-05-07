package cn.learning.hikaricp.controller;

import cn.learning.hikaricp.entity.User;
import cn.learning.hikaricp.service.UserService;
import cn.learning.hikaricp.thread.AsyncService;
import cn.learning.hikaricp.thread.UserThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author clare
 * @Date 2019/5/6 21:51
 * @Version 1.0
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserThread userThread;

    @Autowired
    private AsyncService asyncService;

    @GetMapping(value = "get")
    public String get() {
        User user = new User(1111, "chenshuang", 12, 12);
        userService.addUser(user);
        return "success";
    }


    @GetMapping(value = "add")
    public String add() throws Exception {
       // userService.add();
        new Thread(userThread).start();
        new Thread(userThread).start();
        return "success";
    }

    @GetMapping(value = "userThread1")
    public String userThread1() throws InterruptedException {
        // userService.add();
        asyncService.readCommit(13);
        asyncService.readCommit(13);
        return "success";
    }
}
