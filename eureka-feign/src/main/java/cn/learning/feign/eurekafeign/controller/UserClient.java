package cn.learning.feign.eurekafeign.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName UserClient
 * @Description TODO
 * @Author clare
 * @Date 2019/5/15 14:35
 * @Version 1.0
 */

@FeignClient(value="eureka-provider")
public interface UserClient {

    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    Object getUser(int id);

}
