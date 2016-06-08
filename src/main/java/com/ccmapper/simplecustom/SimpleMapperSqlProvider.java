package com.ccmapper.simplecustom;

import com.ccmapper.core.AbstractSqlProvider;

public class SimpleMapperSqlProvider extends AbstractSqlProvider{

	public SimpleMapperSqlProvider(String className) {
		super(className);
	}

	public String getAgeById(Object key){
		System.out.println(this.beanClazz.getName());
		return "select age from demo where id = #{param1}";
	}
}
