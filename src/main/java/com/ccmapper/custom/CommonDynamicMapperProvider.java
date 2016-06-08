package com.ccmapper.custom;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.ccmapper.core.CustomCommonSqlProvider;
import com.ccmapper.core.example.Example;
import com.ccmapper.core.utils.BeanAndTableUtils;

/**
 * 动态mapper 本类为实现动态的mapper 为基础的table实现增删改查。
 * 
 * @Description: CommonDynamicMapper 可以自己缓存sql 提升性能
 * @author xiaoruihu 2016年5月20日 下午3:14:40
 */
public abstract class CommonDynamicMapperProvider extends CustomCommonSqlProvider {

	public CommonDynamicMapperProvider(String className) {
		super(className);
		this.tableName = this.beanClazz.getSimpleName();
		propertyAndColumnMap = BeanAndTableUtils.getAllPropertyAndColumnMap(beanClazz);
	}

	public String getListByExample(Example example){
		BEGIN();
		SELECT(allSelect(beanClazz));
		FROM(tableName);
		
		WHERE(example.generateWhereSql(propertyAndColumnMap));
		String sql = SQL();
		System.out.println(sql);
		return sql;
		
	}
	
}
