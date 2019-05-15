package cn.learning.feign.eurekafeign.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName UserClient
 * @Description TODO
 * @Author clare
 * @Date 2019/5/15 14:35
 * @Version 1.0
 */

@FeignClient(value="eureka-provider")
public interface UserClient {

    @GetMapping(value = "/getUser/{id}")
    Object getUser(@PathVariable int id);

}
