package com.zxrh.ehomework.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zxrh.ehomework.pojo.Material.Carrier;

public class MaterialCarrierTypeHandler extends BaseTypeHandler<Carrier>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Carrier parameter, JdbcType jdbcType)
			throws SQLException{
		ps.setInt(i,parameter.getCode());
	}

	@Override
	public Carrier getNullableResult(ResultSet rs, String columnName) throws SQLException{
		return Carrier.getByCode(rs.getInt(columnName));
	}

	@Override
	public Carrier getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		return Carrier.getByCode(rs.getInt(columnIndex));
	}

	@Override
	public Carrier getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		return Carrier.getByCode(cs.getInt(columnIndex));
	}

}
