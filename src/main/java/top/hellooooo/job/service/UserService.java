package top.hellooooo.job.service;

import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.User;

import java.util.List;

/**
 * @Author Q
 * @Date 10/01/2021 21:06
 * @Description
 */
public interface UserService {

    // 插入用户
    void insertUser(User user);

    void batchInsertUser(List<User> users);

    void updateUser(User user);

    Clazz getClazzByClazzId(Integer clazzId);

    User getUserByUsername(String username);

    void updateClazz(Clazz clazz);
}
