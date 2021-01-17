package top.hellooooo.job.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Q
 * @Date 2021-01-17 13:08
 * @Description
 */
@Data
@NoArgsConstructor
public class CommonResult<T> {
    private T data;
    private boolean success;
    private String msg;

    public static CommonResult ok(String msg) {
        CommonResult commonResult = new CommonResult();
        commonResult.setData(null);
        commonResult.setSuccess(true);
        commonResult.setMsg(msg);
        return commonResult;
    }

    public static <E> CommonResult ok(E data, String msg) {
        CommonResult<E> commonResult = new CommonResult();
        commonResult.setData(data);
        commonResult.setSuccess(true);
        commonResult.setMsg(msg);
        return commonResult;
    }

    public static <E> CommonResult fail(String msg) {
        CommonResult<E> commonResult = new CommonResult();
        commonResult.setData(null);
        commonResult.setSuccess(false);
        commonResult.setMsg(msg);
        return commonResult;
    }

    public static <E> CommonResult fail(E data, String msg) {
        CommonResult<E> commonResult = new CommonResult();
        commonResult.setData(data);
        commonResult.setSuccess(false);
        commonResult.setMsg(msg);
        return commonResult;
    }
}
