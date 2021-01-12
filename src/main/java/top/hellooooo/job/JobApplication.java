package top.hellooooo.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Q
 * @Date 09/01/2021 15:48
 * @Description
 */

@SpringBootApplication
@MapperScan("top.hellooooo.job.mapper")
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
