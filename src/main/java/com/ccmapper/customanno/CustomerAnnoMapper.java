package com.ccmapper.customanno;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CustomerAnnoMapper<T> {

	@InsertProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "insert")
	public void insert(T t);

	@UpdateProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "updateByPrimaryKey")
	public void update(T t);

	@UpdateProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "updateNotNullByPrimaryKey")
	public void updateNotNull(T t);

	@SelectProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "selectByPrimaryKey")
	public T getByPrimaryKey(Object key);

	@SelectProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "selectByPropertyEqual")
	public T getByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "selectByPropertyEqual")
	public List<T> getListByPropertyEqual(String property, Object value);
	
	@SelectProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "selectAll")
	public List<T> getAll();
	
	@SelectProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "selectByPrimaryKey")
	public Map<String, Object> getMapByPrimaryKey(Object key);
	
	@SelectProvider(type = CustomannoDynamicMapperSqlProvider.class, method = "selectAll")
	public List<Map<String, Object>> getMapListAll();

}
