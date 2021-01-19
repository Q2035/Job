package top.hellooooo.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.job.mapper.LogMapper;
import top.hellooooo.job.pojo.UserActionInfo;
import top.hellooooo.job.service.BaseLog;
import top.hellooooo.job.service.LogService;

/**
 * @Author Q
 * @Date 2021-01-17 11:19
 * @Description
 */
@Service
public class LogServiceImpl extends BaseLog implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public void info(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage(log("info", msg, args));
        logMapper.info(userActionInfo);
    }

    @Override
    public void login(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage(log("login", msg, args));
        logMapper.info(userActionInfo);
    }

    @Override
    public void upload(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage(log("upload", msg, args));
        logMapper.info(userActionInfo);
    }

    @Override
    public void importUser(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage(log("user import", msg, args));
        logMapper.info(userActionInfo);
    }

    @Override
    public void logout(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage(log("logout", msg, args));
        logMapper.info(userActionInfo);
    }
}
