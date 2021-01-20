package top.hellooooo.job.service;

import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.SecurityURI;
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

    void batchInsertUserWithClazzId(List<User> users, Integer clazzId);

    void updateUser(User user);

    Clazz getClazzByClazzId(Integer clazzId);

    User getUserByUsername(String username);

    List<User> getUsers(Integer clazzId);

    List<Clazz> getClazzsByAdminId(Integer id);

    void updateClazz(Clazz clazz);

    void insertClazz(Clazz clazz,Integer userId);

    List<SecurityURI> getSecurityURI();

    User getUserByUserId(Integer userId);
}
