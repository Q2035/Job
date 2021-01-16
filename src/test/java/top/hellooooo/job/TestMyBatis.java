package top.hellooooo.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.hellooooo.job.mapper.UserMapper;
import top.hellooooo.job.pojo.Role;
import top.hellooooo.job.pojo.SecurityURI;
import top.hellooooo.job.pojo.User;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-16 13:07
 * @Description
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMyBatis {

    @Autowired
    UserMapper userMapper;

    @Test
    public void t1(){
        // User q = userMapper.getUserByUsername("Q");
        // System.out.println(q);
        List<SecurityURI> securityURI = userMapper.getSecurityURI();
        for (SecurityURI uri : securityURI) {
            System.out.println(uri);
        }
    }
}
