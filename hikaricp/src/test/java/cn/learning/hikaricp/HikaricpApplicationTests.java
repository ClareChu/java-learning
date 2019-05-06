package cn.learning.hikaricp;

import cn.learning.hikaricp.service.UserService;
import cn.learning.hikaricp.thread.UserThread;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HikaricpApplicationTests {

    @Autowired
    private UserThread userThread;

    @Test
    public void contextLoads() {
        new Thread(userThread).start();
        new Thread(userThread).start();

    }

}
