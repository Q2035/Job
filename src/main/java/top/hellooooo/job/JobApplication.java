package top.hellooooo.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.hellooooo.job.filter.TokenFilter;

import java.text.SimpleDateFormat;

/**
 * @Author Q
 * @Date 09/01/2021 15:48
 * @Description
 */
@SpringBootApplication
@EnableConfigurationProperties(TokenFilter.class)
@MapperScan("top.hellooooo.job.mapper")
public class JobApplication {

    @Bean
    public SimpleDateFormat simpleDateFormat(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat;
    }

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
