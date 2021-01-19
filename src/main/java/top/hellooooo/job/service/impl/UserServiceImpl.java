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
     * 批量插入用户到指定班级
     *
     * @param users
     */
    @Override
    public void batchInsertUserWithClazzId(List<User> users, Integer clazzId) {
        if (users == null) {
            throw new NullPointerException("BatchInsert: users is null");
        }
        // 先插入j_user用户表
        userMapper.batchInsertUser(users);
        // 再插入j_user_clazz表
        if (!users.isEmpty()) {
            users.forEach(user->{
                userMapper.insertUserClazz(user.getId(), clazzId);
            });
        }
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
    public List<User> getUsers(Integer clazzId) {
        return userMapper.getUsersByClazzId(clazzId);
    }

    @Override
    public List<Clazz> getClazzsByAdminId(Integer id) {
        return userMapper.getClazzsByAdminId(id);
    }

    @Override
    public void updateClazz(Clazz clazz) {
        userMapper.updateClazz(clazz);
    }

    @Override
    public void insertClazz(Clazz clazz,Integer userId) {
        if (clazz.getStudentCount() == null) {
            clazz.setStudentCount(0);
        }
        userMapper.insertClazz(clazz);
        if (clazz.getId() != null) {
            userMapper.insertClazzAdminInfo(clazz.getId(), userId);
            return;
        }
        throw new RuntimeException("The Clazz Id is empty from db");
    }

    @Override
    public List<SecurityURI> getSecurityURI() {
        return userMapper.getSecurityURI();
    }

}
