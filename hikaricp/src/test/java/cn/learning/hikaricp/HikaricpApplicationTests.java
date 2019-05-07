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
    public void readCommit() throws InterruptedException {
        asyncService.readCommit();
        asyncService.readCommit();
        Thread.sleep(100000 );
    }

    @Test
    public void readUnCommit() throws InterruptedException {
        asyncService.readUnCommit();
        asyncService.readUnCommit();
        Thread.sleep(100000);
    }


    @Test
    public void repeatable() throws InterruptedException {
        asyncService.repeatable();
        asyncService.repeatable();
        Thread.sleep(100000);
    }

    @Test
    public void serializable() throws InterruptedException {
        asyncService.serializable();
        asyncService.serializable();
        Thread.sleep(100000);
    }
}
