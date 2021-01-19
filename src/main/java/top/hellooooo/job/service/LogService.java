package top.hellooooo.job.service;

import top.hellooooo.job.pojo.User;
import top.hellooooo.job.pojo.UserActionInfo;

/**
 * @Author Q
 * @Date 2021-01-17 11:19
 * @Description
 */
public interface LogService {

    void info(UserActionInfo userActionInfo, String msg, String... args);

    void login(UserActionInfo userActionInfo, String msg, String... args);

    void upload(UserActionInfo userActionInfo, String msg, String... args);

    void importUser(UserActionInfo userActionInfo, String msg, String... args);

    void logout(UserActionInfo userActionInfo, String msg, String... args);
}
