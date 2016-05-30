package com.ccmapper.custom;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CommonMapper<E> {

	@InsertProvider(type = CommonDynamicMapperProvider.class, method = "insert")
	public void insert(E t);

	@UpdateProvider(type = CommonDynamicMapperProvider.class, method = "updateByPrimaryKey")
	public void update(E t);

	@UpdateProvider(type = CommonDynamicMapperProvider.class, method = "updateNotNullByPrimaryKey")
	public void updateNotNull(E t);

	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPrimaryKey")
	public E getByPrimaryKey(Object key);

	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public E getByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public List<E> getListByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectAll")
	public List<E> getAll();
	
	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPrimaryKey")
	public Map<String, Object> getMapByPrimaryKey(Object key);
	
	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectAll")
	public List<Map<String, Object>> getMapListAll();

}
