package top.hellooooo.job.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Author Q
 * @Date 09/01/2021 15:51
 * @Description 一个班级可以有多个学生，一个学生可以同属多个班级
 */
@Data
public class User {

    private Integer id;

    // 学号
    private String username;

    // 真实姓名
    private String realName;

    private String password;

    // 网名
    private String nickname;

    // 头像
    private String avatar;

    // 邮箱地址
    private String email;

    // 用户角色信息
    private Role role;

    // 所属班级
    private List<Clazz> clazz;

    // 账户状态
    private AccountStatus accountStatus;
}
