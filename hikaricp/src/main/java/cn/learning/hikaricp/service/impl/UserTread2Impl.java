package cn.learning.hikaricp.service.impl;

import cn.learning.hikaricp.entity.User;
import cn.learning.hikaricp.mapper.UserMapper;
import cn.learning.hikaricp.service.UserThread2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UserTread2Impl
 * @Description TODO
 * @Author clare
 * @Date 2019/5/7 17:02
 * @Version 1.0
 */
@Slf4j
@Service
public class UserTread2Impl implements UserThread2 {

    @Resource
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public void read(int id, Object o) throws InterruptedException {
        Thread.sleep(10000);
        synchronized (o) {
            log.info("read2 thread name:{}", Thread.currentThread().getName());
            User user = userMapper.findUserById(id);
            log.info("find user by id: {}, thread name:{}", user.toString(), Thread.currentThread().getName());
            log.info("notify all");
            o.notifyAll();
        }
    }
}
