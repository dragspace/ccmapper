package com.ccmapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CommonMapper<T> {

	@InsertProvider(type = CommonDynamicMapperProvider.class, method = "insert")
	public void insert(T t);

	@UpdateProvider(type = CommonDynamicMapperProvider.class, method = "updateByPrimaryKey")
	public void update(T t);

	@UpdateProvider(type = CommonDynamicMapperProvider.class, method = "updateNotNullByPrimaryKey")
	public void updateNotNull(T t);

	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPrimaryKey")
	public T getByPrimaryKey(Object key);

	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public T getByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public List<T> getListByPropertyEqual(String property, Object value);

}
