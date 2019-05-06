package cn.learning.hikaricp.service;

import cn.learning.hikaricp.entity.User;
import cn.learning.hikaricp.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author clare
 * @Date 2019/5/6 21:00
 * @Version 1.0
 */
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;


    public User getUserById(String userName) {
        return userMapper.findUserByName(userName);
    }

    public boolean addUser(User record){
        boolean result = false;
        try {
            userMapper.insertUser(record);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
