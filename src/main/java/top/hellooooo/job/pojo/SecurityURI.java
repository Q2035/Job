package top.hellooooo.job.pojo;

import lombok.Data;

/**
 * @Author Q
 * @Date 2021-01-16 16:37
 * @Description
 */
@Data
public class SecurityURI {
    private Integer id;
    private String uri;
    // 访问该链接的最小角色
    private Role roles;
}
