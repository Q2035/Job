package top.hellooooo.job.mapper;

import top.hellooooo.job.pojo.UserActionInfo;

/**
 * @Author Q
 * @Date 2021-01-17 10:50
 * @Description
 */
public interface LogMapper {
    /**
     * 根据用户行为插入日志信息
     * @param userActionInfo
     */
    void info(UserActionInfo userActionInfo);
}
