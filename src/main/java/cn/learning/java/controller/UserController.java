package cn.learning.java.controller;

import cn.learning.java.entity.User;
import cn.learning.java.service.UserService;
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

    @GetMapping(name = "get")
    public String get() {
        User user = new User(1111, "chenshuang", 12, 12);
        userService.addUser(user);
        return "success";
    }

}
