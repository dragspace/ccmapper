package com.ccmapper.custom;

import com.ccmapper.core.CustomCommonSqlProvider;
import com.ccmapper.core.utils.BeanAndTableUtils;

/**
 * 动态mapper 本类为实现动态的mapper 为基础的table实现增删改查。
 * 
 * @Description: CommonDynamicMapper 可以自己缓存sql 提升性能
 * @author xiaoruihu 2016年5月20日 下午3:14:40
 */
public abstract class CustomDynamicMapperProvider extends CustomCommonSqlProvider {

	public CustomDynamicMapperProvider(String className) {
		super(className);
		this.tableName = this.beanClazz.getSimpleName();
		propertyAndColumnMap = BeanAndTableUtils.getAllPropertyAndColumnMap(beanClazz);
		this.primaryKey = "id";
	}

}
