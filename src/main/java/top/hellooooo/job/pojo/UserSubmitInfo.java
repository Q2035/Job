package top.hellooooo.job.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author Q
 * @Date 2021-01-21 21:47
 * @Description
 */
@Data
public class UserSubmitInfo {
    private Integer id;
    private Integer userId;
    private Integer jobId;
    /**
     * 作业提交时间
     */
    private Date submitTime;
}
