package top.hellooooo.job.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import top.hellooooo.job.pojo.AccountStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Q
 * @Date 1/11/2021 6:37 PM
 * @Description 自定义类型处理器
 */
// 实验证明 不加上也没事 参考网址：https://segmentfault.com/a/1190000019168232
//在 xml 中添加该 TypeHandler 时需要使用该注解
// @MappedTypes(value = {
//         AccountStatus.class
// })
public class AccountStatusTypeHandler<E extends AccountStatus> extends BaseTypeHandler<AccountStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AccountStatus parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            return;
        }
        ps.setInt(i, parameter.getStatus());
    }

    @Override
    public AccountStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int accountStatus = rs.getInt(columnName);
        return getAccountStatus(columnName, accountStatus);
    }

    @Override
    public AccountStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int accountStatus = rs.getInt(columnIndex);
        return getAccountStatus(columnIndex, accountStatus);
    }

    @Override
    public AccountStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int accountStatus = cs.getInt(columnIndex);
        return getAccountStatus(columnIndex, accountStatus);
    }

    private AccountStatus getAccountStatus(Object columnIndex, int accountStatus) {
        switch (accountStatus) {
            case 0:
                return AccountStatus.ACCOUNT_NORMAL;
            case 1:
                return AccountStatus.ACCOUNT_FREEZE;
            case 2:
                return AccountStatus.ACCOUNT_DISABLE;
            default:
                throw new IllegalArgumentException(
                        "Cannot convert [" + columnIndex + "] with AccountStatus");
        }
    }
}
