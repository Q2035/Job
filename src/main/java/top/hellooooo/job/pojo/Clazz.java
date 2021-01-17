package top.hellooooo.job.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Author Q
 * @Date 09/01/2021 15:58
 * @Description
 */
@Data
public class Clazz {

    private Integer id;

    // 班级名
    private String clazzName;

    // 班级人数
    private Integer studentCount;

    // 班级学生
    private List<User> students;

    // 班级管理员们
    private List<User> admins;
}
