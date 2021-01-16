package top.hellooooo.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.job.mapper.UserMapper;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.SecurityURI;
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


    /**
     * 批量插入用户
     * @param users
     */
    @Override
    public void batchInsertUser(List<User> users) {
        if (users == null) {
            throw new NullPointerException("BatchInsert: users is null");
        }
        userMapper.batchInsertUser(users);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public Clazz getClazzByClazzId(Integer clazzId) {
        return userMapper.getClazzByClazzId(clazzId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public void updateClazz(Clazz clazz) {
        userMapper.updateClazz(clazz);
    }

    @Override
    public List<SecurityURI> getSecurityURI() {
        return userMapper.getSecurityURI();
    }

}
