package com.ccmapper.customsimple;

import org.apache.ibatis.annotations.SelectProvider;

public interface SimpleCommonMapper<T> {

	
	@SelectProvider(type = SimpleMapperSqlProvider.class, method = "getAgeById")
	public String getAgeById(Object key);

	
	@SelectProvider(type = SimpleMapperSqlProvider.class, method = "getById")
	public T getById(Object key);

}
