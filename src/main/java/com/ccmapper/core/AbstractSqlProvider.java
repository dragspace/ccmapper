package com.ccmapper.core;

import java.util.Map;

import com.ccmapper.core.utils.BeanAndTableUtils;

public abstract class AbstractSqlProvider {

	protected Map<String, String> propertyAndColumnMap = null;
	protected Class<?> clazz;
	protected String tableName;
	protected final String primaryKey = "id";
	
	public AbstractSqlProvider(String className){
		try {
			this.clazz = Class.forName(className);
			this.tableName = this.clazz.getSimpleName();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(String.format("构造动态sql提供者类失败 参数 tablename %s classname %s",tableName, className), e);
		}
		propertyAndColumnMap = BeanAndTableUtils.getAllPropertyAndColumnMap(clazz);
	}
	
	
	
	
}
