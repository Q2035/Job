package top.hellooooo.job.mapper;

import top.hellooooo.job.pojo.User;

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
}
