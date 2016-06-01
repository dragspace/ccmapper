package com.ccmapper.core;

public abstract class AbstractSqlProvider {

	protected Class<?> beanClazz;
	
	public AbstractSqlProvider(String className){
		try {
			this.beanClazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(String.format("构造动态sql提供者类失败 参数 classname %s", className), e);
		}
	}
	
	
	
	
}
