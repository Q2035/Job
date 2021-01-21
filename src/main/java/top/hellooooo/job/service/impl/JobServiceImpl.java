package top.hellooooo.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.job.mapper.JobMapper;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.JobInfo;
import top.hellooooo.job.service.JobService;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-21 12:56
 * @Description
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public void createJob(JobInfo jobInfo) {
        if (jobInfo == null || jobInfo.getClazzes() == null) {
            throw new RuntimeException("Sorry, class can't be empty");
        }
        jobMapper.createJob(jobInfo);
        // 将相应的作业信息存到班级中
        jobInfo.getClazzes().forEach(clazz -> jobMapper.insertJobClazz(jobInfo.getId(), clazz.getId()));
    }

    @Override
    public void updateJob(JobInfo jobInfo) {
        jobMapper.updateJob(jobInfo);
    }

    @Override
    public JobInfo getJobById(Integer id) {
        return jobMapper.getJobById(id);
    }

    @Override
    public void insertJobClazz(Integer jobId, Integer clazzId) {
        jobMapper.insertJobClazz(jobId, clazzId);
    }

    @Override
    public List<Clazz> getClazzWithJobId(Integer jobId) {
        return jobMapper.getClazzWithJobId(jobId);
    }

    @Override
    public List<JobInfo> getJobsByCreatorId(Integer userId) {
        return jobMapper.getJobsByCreatorId(userId);
    }
}
