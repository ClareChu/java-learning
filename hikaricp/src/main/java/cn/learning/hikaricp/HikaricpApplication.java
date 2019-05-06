package cn.learning.hikaricp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.learning.java.hikaricp")
public class HikaricpApplication {

    public static void main(String[] args) {
        SpringApplication.run(HikaricpApplication.class, args);
    }

}
