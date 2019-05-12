package cn.learning.eureka.consumer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author clare
 * @Date 2019/5/9 13:41
 * @Version 1.0
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    RestTemplate restTemplate;

    private List<User> users;


    @HystrixCommand(fallbackMethod = "testError")
    @GetMapping(value = "/gotoUser")
    public Map<String, Object> getUser(@RequestParam Integer id) {
        log.info("consumer:{}", id);
        Map<String, Object> data = restTemplate.getForObject("http://eureka-provider/getUser?id=" + id, Map.class);
        return data;
    }

    public String testError() {
        return "出错之后会返回这里";
    }


    @GetMapping(value = "user")
    public Object getUser(int id) {
        return users.stream().filter(user1 -> (user1.getId() == id)).findFirst().get();
    }

    @GetMapping(value = "users")
    public Object getUsers() {
        return users;
    }

    @PostMapping(value = "user")
    public Object postUser(@RequestBody User user) {
        synchronized (user) {
            if (users == null || users.size() == 0) {
                users = new ArrayList<>();
                user.setId(1);
            } else {
                User user1 = users.stream().max((id, id1) -> (id.getId() - id1.getId())).get();
                user.setId(user1.getId() + 1);
            }
            users.add(user);
        }
        return user;
    }

    @PostMapping(value = "deleteUser")
    public Object deleteUser(int id) {
        User user = users.stream().filter(user1 -> (user1.getId() == id)).findFirst().get();
        users.remove(user);
        return users;
    }

}
