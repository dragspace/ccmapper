package com.ccmapper.custom;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.ccmapper.core.example.Example;

public interface CustomMapper<T> {

	@InsertProvider(type = CustomDynamicMapperProvider.class, method = "insert")
	public void insert(T t);

	@UpdateProvider(type = CustomDynamicMapperProvider.class, method = "updateByPrimaryKey")
	public void update(T t);

	@UpdateProvider(type = CustomDynamicMapperProvider.class, method = "updateNotNullByPrimaryKey")
	public void updateNotNull(T t);

	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectByPrimaryKey")
	public T getByPrimaryKey(Object key);

	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public T getByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public List<T> getListByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectAll")
	public List<T> getAll();
	
	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectByPrimaryKey")
	public Map<String, Object> getMapByPrimaryKey(Object key);
	
	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectAll")
	public List<Map<String, Object>> getMapListAll();
	
	@SelectProvider(type = CustomDynamicMapperProvider.class, method = "selectListByExample")
	public List<T> getListByExample(Example example);
	
	

}
