package cn.learning.hikaricp.service;

import cn.learning.hikaricp.entity.User;

/**
 * @ClassName UserThread2
 * @Description TODO
 * @Author clare
 * @Date 2019/5/7 16:59
 * @Version 1.0
 */
public interface UserThread2 {

    void read(int id, Object o) throws InterruptedException;

    void read1(int id) throws InterruptedException;

    boolean addUser(User record) throws InterruptedException;
}
