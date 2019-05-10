package cn.learning.eureka.consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

    @GetMapping(value = "/gotoUser")
    public Map<String,Object> getUser(@RequestParam Integer id){
        log.info("consumer:{}", id);
        Map<String,Object> data = restTemplate.getForObject("http://eureka-provider/getUser?id="+id,Map.class);
        return data;
    }

}
