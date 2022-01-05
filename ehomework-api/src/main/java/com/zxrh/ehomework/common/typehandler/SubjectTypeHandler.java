package com.zxrh.ehomework.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zxrh.ehomework.pojo.Subject;

public class SubjectTypeHandler extends BaseTypeHandler<Subject>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Subject parameter, JdbcType jdbcType) throws SQLException{
		ps.setInt(i,parameter.getCode());
	}

	@Override
	public Subject getNullableResult(ResultSet rs, String columnName) throws SQLException{
		int code = rs.getInt(columnName);
		return Subject.getByCode(code);
	}

	@Override
	public Subject getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		int code = rs.getInt(columnIndex);
		return Subject.getByCode(code);
	}

	@Override
	public Subject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		int code = cs.getInt(columnIndex);
		return Subject.getByCode(code);
	}
	
}