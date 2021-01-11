package top.hellooooo.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.job.mapper.UserMapper;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.service.UserService;

import java.util.List;

/**
 * @Author Q
 * @Date 1/11/2021 3:08 PM
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public void batchInsertUser(List<User> users) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public Clazz getClazzByClazzId(Integer clazzId) {
        return null;
    }

    @Override
    public User getUserByUsername() {
        return null;
    }
}
