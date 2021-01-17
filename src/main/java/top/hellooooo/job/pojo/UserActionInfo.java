package top.hellooooo.job.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Q
 * @Date 10/01/2021 19:14
 * @Description 用户操作日志记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserActionInfo {

    private Integer id;

    // 操作者
    private Integer userId;

    private String username;

    // 操作详情
    private String message;

}
