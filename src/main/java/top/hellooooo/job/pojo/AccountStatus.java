package top.hellooooo.job.pojo;

/**
 * @Author Q
 * @Date 09/01/2021 16:11
 * @Description
 */
public enum AccountStatus {

    /**
     * 0正常 1冻结 2禁用
     * 2可用于进行逻辑删除
     */
    ACCOUNT_NORMAL(0), ACCOUNT_FREEZE(1), ACCOUNT_DISABLE(2);

    Integer status;

    AccountStatus(Integer status) {
        this.status = status;
    }
}
