package top.hellooooo.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.hellooooo.job.mapper.UserMapper;
import top.hellooooo.job.pojo.User;

/**
 * @Author Q
 * @Date 10/01/2021 21:31
 * @Description
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJobApplication {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void t1(){
        User q = userMapper.getUserByUsername("Q");
        System.out.println(q);
        System.out.println("Hello");
    }
}
