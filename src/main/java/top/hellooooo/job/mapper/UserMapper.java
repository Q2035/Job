package top.hellooooo.job.mapper;

import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.Role;
import top.hellooooo.job.pojo.SecurityURI;
import top.hellooooo.job.pojo.User;

import java.util.List;

/**
 * @Author Q
 * @Date 10/01/2021 21:02
 * @Description
 */
public interface UserMapper {

    User getUserByUsername(String username);

    void updateUser(User user);

    User getUserFromBlackListByUsername(String username);

    /**
     * 将用户插入表中
     * @param user
     */
    void insertUser(User user);

    void batchInsertUser(List<User> users);

    Clazz getClazzByClazzId(Integer id);

    void updateClazz(Clazz clazz);

    void insertClazz(Clazz clazz);

    List<SecurityURI> getSecurityURI();
}