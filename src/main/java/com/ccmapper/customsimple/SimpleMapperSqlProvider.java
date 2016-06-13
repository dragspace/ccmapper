package com.ccmapper.customsimple;

import com.ccmapper.core.AbstractSqlProvider;

public class SimpleMapperSqlProvider extends AbstractSqlProvider{

	public SimpleMapperSqlProvider(String className) {
		super(className);
	}

	public String getNameById(Object key){
		System.out.println(this.beanClazz.getName());
		return "select name from simple where id = #{param1}";
	}
	public String getById(Object key){
		System.out.println(this.beanClazz.getName());
		
		System.out.println(this.beanClazz.getName());
		return "select * from simple where id = #{param1}";
	}
}
