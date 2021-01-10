package top.hellooooo.job.pojo;

/**
 * @Author Q
 * @Date 10/01/2021 19:16
 * @Description
 */
public enum Role {
    /**
     * 管理员 学生 班级管理员
     */
    ADMIN("admin"), STUDENT("student"), CLAZZ_ADMIN("clazz_admin");

    String role;

    Role(String role) {
        this.role = role;
    }
}
