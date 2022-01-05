package com.zxrh.ehomework.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zxrh.ehomework.pojo.Task.Form;

public class TaskFormTypeHandler extends BaseTypeHandler<Form>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Form parameter, JdbcType jdbcType) throws SQLException{
		ps.setInt(i,parameter.getCode());
	}

	@Override
	public Form getNullableResult(ResultSet rs, String columnName) throws SQLException{
		return Form.getByCode(rs.getInt(columnName));
	}

	@Override
	public Form getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		return Form.getByCode(rs.getInt(columnIndex));
	}

	@Override
	public Form getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		return Form.getByCode(cs.getInt(columnIndex));
	}

}