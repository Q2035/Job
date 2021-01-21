package top.hellooooo.job.service;

import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.JobInfo;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-21 12:56
 * @Description
 */
public interface JobService {

    void createJob(JobInfo jobInfo);

    void updateJob(JobInfo jobInfo);

    JobInfo getJobById(Integer id);

    void insertJobClazz(Integer jobId, Integer clazzId);

    List<Clazz> getClazzWithJobId(Integer jobId);

    List<JobInfo> getJobsByCreatorId(Integer userId);
}
