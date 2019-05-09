package cn.learning.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author clare
 * @Date 2019/5/9 13:30
 * @Version 1.0
 */

@RestController
public class UserController {


    @GetMapping(value = "/getUser")
    public Object getUser(int id) {
        Map<String,Object> data = new HashMap<>();
        data.put("id",id);
        data.put("userName","admin");
        data.put("from","provider-A");
        return data;
    }
}
