package top.hellooooo.job.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author Q
 * @Date 09/01/2021 15:53
 * @Description
 */
@Data
public class JobInfo {

    private Integer id;

    // 作业名称
    private String jobName;

    private String separators;

    // 截止时间
    private Date deadline;

    // 作业创建时间
    private Date createTime;

    // 作业名
    private String jobFilenameInfo;


    // 是否开启邮箱提醒
    private Boolean emailRemind;

    // 该作业所分配的班级
    private List<Clazz> clazzes;

    // 该作业发起人
    private User creator;
}