package top.hellooooo.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.hellooooo.job.mapper.UserMapper;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private UserService userService;


    @Test
    public void t1(){
        User q = userMapper.getUserByUsername("Q");
        String str = "你好";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        try {
            String result = new String(bytes, "GBK");
            System.out.println(result);
            System.out.println(EncodingUtils.isMessyCode(result));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // q.setAvatar("https://hellooooo.top/image/blog/many/about.png");
        // q.setUsername("q2");
        //
        // Clazz clazz = userMapper.getClazzByClazzId(1);
        //
        // clazz.setClazzName("CS18");
        // userMapper.updateClazz(clazz);
    }

    @Test
    public void testBatchInsertUser(){
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUsername("QQ");
        user.setRealName("Li");
        user.setNickname("QQ");
        User user2 = new User();
        user2.setUsername("QQ");
        user2.setRealName("Li");
        user2.setNickname("QQ");
        userList.add(user);
        userList.add(user2);

        userService.batchInsertUserWithClazzId(userList, 1);
    }
}
