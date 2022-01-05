package com.zxrh.ehomework.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zxrh.ehomework.pojo.Question.Type;

public class QuestionTypeHandler extends BaseTypeHandler<Type>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Type parameter, JdbcType jdbcType) throws SQLException{
		ps.setInt(i,parameter.getCode());
	}

	@Override
	public Type getNullableResult(ResultSet rs, String columnName) throws SQLException{
		int code = rs.getInt(columnName);
		return Type.getByCode(code);
	}

	@Override
	public Type getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		int code = rs.getInt(columnIndex);
		return Type.getByCode(code);
	}

	@Override
	public Type getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		int code = cs.getInt(columnIndex);
		return Type.getByCode(code);
	}

}
