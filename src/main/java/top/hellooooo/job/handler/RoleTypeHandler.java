package top.hellooooo.job.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import top.hellooooo.job.pojo.Role;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Q
 * @Date 2021-01-16 14:12
 * @Description
 */
public class RoleTypeHandler<E extends Role> extends BaseTypeHandler<Role> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Role parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setInt(i, Role.STUDENT.getRole());
        }
        ps.setInt(i, parameter.getRole());
    }

    @Override
    public Role getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int role = rs.getInt(columnName);
        return getRole(columnName, role);
    }

    @Override
    public Role getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int role = rs.getInt(columnIndex);
        return getRole(columnIndex, role);
    }

    @Override
    public Role getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int role = cs.getInt(columnIndex);
        return getRole(columnIndex, role);
    }

    private Role getRole(Object columnIndex, int role) {
        switch (role) {
            case 4:
                return Role.ADMIN;
            case 3:
                return Role.CLAZZ_ADMIN;
            case 2:
            case 0:
                return Role.STUDENT;
            case 1:
                return Role.ANONYMOUS;
            default:
                throw new IllegalArgumentException(
                        "Cannot convert [" + columnIndex + "] with Role");
        }
    }
}
