package com.zxrh.ehomework.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zxrh.ehomework.pojo.Homework.Status;

public class HomeworkStatusHandler extends BaseTypeHandler<Status>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Status parameter, JdbcType jdbcType)
			throws SQLException{
		ps.setInt(i,parameter.getCode());
	}

	@Override
	public Status getNullableResult(ResultSet rs, String columnName) throws SQLException{
		int code = rs.getInt(columnName);
		return Status.getByCode(code);
	}

	@Override
	public Status getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		int code = rs.getInt(columnIndex);
		return Status.getByCode(code);
	}

	@Override
	public Status getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		int code = cs.getInt(columnIndex);
		return Status.getByCode(code);
	}

}
