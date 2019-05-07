package cn.learning.hikaricp;

import cn.learning.hikaricp.thread.AsyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HikaricpApplicationTests {

    @Autowired
    private AsyncService asyncService;

    @Test
    public void contextLoads() throws InterruptedException {
        asyncService.executeAsync1();
        asyncService.executeAsync2();
        Thread.sleep(100000 );
    }

}
