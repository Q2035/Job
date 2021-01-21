package top.hellooooo.job.mapper;

import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.JobInfo;

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

    void insertJobClazz(Integer jobId, Integer clazzId);

    List<Clazz> getClazzWithJobId(Integer jobId);

    List<JobInfo> getJobsByCreatorId(Integer userId);

}
