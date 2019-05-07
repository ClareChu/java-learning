package cn.learning.hikaricp.service.impl;

import cn.learning.hikaricp.entity.User;
import cn.learning.hikaricp.mapper.UserMapper;
import cn.learning.hikaricp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author clare
 * @Date 2019/5/6 21:00
 * @Version 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;


    public User getUserById(String userName) {
        return userMapper.findUserByName(userName);
    }

    public boolean addUser(User record) {
        boolean result = false;
        try {
            userMapper.insertUser(record);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<User> getAll() {
        return userMapper.findAllUser();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void readUnCommit() throws InterruptedException {
        add();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void readCommit() throws InterruptedException {
        add();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void repeatable() throws InterruptedException {
        add();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void serializable() throws InterruptedException {
        add();
    }

    public void add() throws InterruptedException {
        log.info("add method thread name:{}", Thread.currentThread().getName());
        User user = userMapper.findUserById(13);
        log.info("find user by id: {}, thread name:{}", user.toString(), Thread.currentThread().getName());
        userMapper.updateUser(user);
        log.info("sleep time 3m , thread name:{}", Thread.currentThread().getName());
        //休眠3秒
        Thread.sleep(3000);
        User user1 = userMapper.findUserById(13);
        log.info("after user money find user by id: {} , thread name:{}", user1.toString(), Thread.currentThread().getName());
    }
}
