package top.hellooooo.job.pojo;

/**
 * @Author Q
 * @Date 10/01/2021 19:16
 * @Description
 */
public enum Role {

    /**
     * 管理员 学生 班级管理员 匿名用户
     */
    ADMIN(4), CLAZZ_ADMIN(3), STUDENT(2), ANONYMOUS(1);

    Integer role;

    Role(Integer role) {
        this.role = role;
    }

    public Integer getRole() {
        return role;
    }

    public static Role fromRoleName(Integer roleName) {
        for (Role role : Role.values()) {
            if (role.getRole() == roleName) {
                return role;
            }
        }
        return null;
    }
}
