package cn.learning.java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.learning.java.mapper")
public class JavaLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaLearningApplication.class, args);
    }

}
