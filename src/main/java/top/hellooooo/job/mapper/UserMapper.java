package top.hellooooo.job.mapper;

import io.lettuce.core.dynamic.annotation.Param;
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

    User getUserByUserId(Integer id);

    void updateUser(User user);

    User getUserFromBlackListByUsername(String username);

    /**
     * 将用户插入表中
     * @param user
     */
    void insertUser(User user);

    void insertUserClazz(Integer userId, Integer clazzId);

    void batchInsertUser(@Param("list") List<User> users);

    Clazz getClazzByClazzId(Integer id);

    List<Clazz> getClazzsByAdminId(Integer id);

    List<User> getUsersByClazzId(Integer clazzId);

    void updateClazz(Clazz clazz);

    void insertClazzAdminInfo(@Param("clazzId") Integer clazzId, @Param("userId") Integer userId);

    Integer insertClazz(Clazz clazz);

    List<SecurityURI> getSecurityURI();
}