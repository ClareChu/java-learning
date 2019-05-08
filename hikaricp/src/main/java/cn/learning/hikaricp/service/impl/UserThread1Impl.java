package cn.learning.hikaricp.service.impl;

import cn.learning.hikaricp.entity.User;
import cn.learning.hikaricp.mapper.UserMapper;
import cn.learning.hikaricp.service.UserThread1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserThread1Impl
 * @Description TODO
 * @Author clare
 * @Date 2019/5/7 17:01
 * @Version 1.0
 */
@Service
@Slf4j
public class UserThread1Impl implements UserThread1 {
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED,rollbackFor = RuntimeException.class)
    public void read(int id, Object o) throws InterruptedException, RuntimeException {
        synchronized (o) {
            log.info("read1 thread name:{}", Thread.currentThread().getName());
            userMapper.findUserById(id);
            User user = userMapper.findUserById(id);
            log.info("find user by id: {}, thread name:{}", user.toString(), Thread.currentThread().getName());
            int returncode = userMapper.updateUser(user);
            log.info("wait thread name {}", Thread.currentThread().getName());
            o.wait();
            throw new RuntimeException("回滚");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void read1(int id) throws InterruptedException {
        log.info("add method thread name:{}", Thread.currentThread().getName());
        List<User> user = userMapper.findAllUser();
        log.info("find user by id: {}, thread name:{}", user.get(1).toString(), Thread.currentThread().getName());
        //休眠3秒
        Thread.sleep(9000);
        User user1 = userMapper.findUserById(id);
        log.info("after user money find user by id: {} , thread name:{}", user1.toString(), Thread.currentThread().getName());
    }
}
