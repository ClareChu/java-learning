package cn.learning.java;

import cn.learning.java.entity.User;
import cn.learning.java.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaLearningApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        User user = new User(1111, "chenshuang", 12, 12);
        userService.addUser(user);
    }

}
