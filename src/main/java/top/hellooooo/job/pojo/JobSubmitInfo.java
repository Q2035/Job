package top.hellooooo.job.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author Q
 * @Date 09/01/2021 16:03
 * @Description 作业提交详情
 */
@Data
public class JobSubmitInfo {

    private Integer id;

    // 作业提交时间
    private Date submitTime;


}
