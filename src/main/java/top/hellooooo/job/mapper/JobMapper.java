package top.hellooooo.job.mapper;

import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.JobInfo;
import top.hellooooo.job.pojo.UserSubmitInfo;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-21 11:27
 * @Description
 */
public interface JobMapper {

    void createJob(JobInfo jobInfo);

    void updateJob(JobInfo jobInfo);

    JobInfo getJobById(Integer id);

    /**
     * 通过班级ID获取所有的JobInfo信息
     * @param clazzId
     * @return
     */
    List<JobInfo> getJobsByClazzId(Integer clazzId);

    void insertJobClazz(Integer jobId, Integer clazzId);

    List<Clazz> getClazzWithJobId(Integer jobId);

    List<JobInfo> getJobsByCreatorId(Integer userId);

    /**
     * 根据作业ID查看用户提交情况
     * @param jobId
     * @return
     */
    List<UserSubmitInfo> getSubmitInfoByJobId(Integer jobId);

    /**
     * 查看指定用户的作业提交情况
     * @param userId
     * @return
     */
    List<UserSubmitInfo> getSubmitInfoByUserId(Integer userId);

    UserSubmitInfo getSubmitInfoByUserIdAndJobId(Integer userId, Integer jobId);

    void insertSubmitInfo(UserSubmitInfo userSubmitInfo);

    void updateSubmitInfo(UserSubmitInfo userSubmitInfo);

}
