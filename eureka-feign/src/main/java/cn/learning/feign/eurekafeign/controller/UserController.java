package cn.learning.feign.eurekafeign.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author clare
 * @Date 2019/5/15 14:25
 * @Version 1.0
 */
@RestController
@FeignClient(value="feign-client")
@Slf4j
public class UserController {

    @Autowired
    private UserClient userClient;

    @GetMapping(value = "/gotoUser")
    public Map<String, Object> getUser(@RequestParam Integer id) {
        log.info("consumer:{}", id);
        Map<String, Object> data = (Map<String, Object>) userClient.getUser(id);
        return data;
    }


}
