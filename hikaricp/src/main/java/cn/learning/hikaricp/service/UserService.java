package cn.learning.hikaricp.service;

import cn.learning.hikaricp.entity.User;
import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author clare
 * @Date 2019/5/6 21:00
 * @Version 1.0
 */

public interface UserService {

     User getUserById(String userName);

     boolean addUser(User record);

     List<User> getAll();

     void readUnCommit() throws InterruptedException;

     void readCommit() throws InterruptedException;

     void repeatable() throws InterruptedException;

     void serializable() throws InterruptedException;

}
