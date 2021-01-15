package top.hellooooo.job.exception;

/**
 * @Author Q
 * @Date 2021-01-15 18:55
 * @Description 认证失败 密码错误 用户不存在抛出此异常
 */
public class AuthenticateException extends Exception {

    private Integer errCode;
    private String errMsg;

    public AuthenticateException() {
        super();
    }

    public AuthenticateException(Integer errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public AuthenticateException(String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
    }
}
