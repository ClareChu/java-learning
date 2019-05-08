package cn.learning.hikaricp.service.impl;

import cn.learning.hikaricp.entity.User;
import cn.learning.hikaricp.mapper.UserMapper;
import cn.learning.hikaricp.service.UserThread2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
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


    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

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

    @Transactional
    public void read1(int id) throws InterruptedException {
        //休眠2秒
        Thread.sleep(2000);
        int returncode = userMapper.updateByMoney(id);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        User user1 = userMapper.findUserById(id);
        log.info("after user money find user by id: {} , thread name:{}", user1.toString(), Thread.currentThread().getName());
    }
}
