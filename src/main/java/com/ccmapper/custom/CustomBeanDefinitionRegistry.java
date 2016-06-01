package com.ccmapper.custom;

import java.util.Arrays;
import java.util.List;

import com.ccmapper.core.AbstractCCMapperBeanDefinitionRegistry;
import com.demo.bean.Demo;
import com.demo.bean.Demo2;
import com.demo.bean.Demo4;

public class CustomBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry {

	private Class<?> defaultSqlProvider = CommonDynamicMapperProvider.class;
	private Class<?> defaultCommonMapper = CommonMapper.class;

	@Override
	public List<Class<?>> getBeanList() {
		return Arrays.asList(Demo.class, Demo2.class, Demo4.class);
	}

	@Override
	public Class<?> getCommonMapper() {
		return defaultCommonMapper;
	}

	@Override
	public Class<?> getSqlProvider() {
		return defaultSqlProvider;
	}

	public void setDefaultSqlProvider(Class<?> defaultSqlProvider) {
		this.defaultSqlProvider = defaultSqlProvider;
	}

	public void setDefaultCommonMapper(Class<?> defaultCommonMapper) {
		this.defaultCommonMapper = defaultCommonMapper;
	}

}
