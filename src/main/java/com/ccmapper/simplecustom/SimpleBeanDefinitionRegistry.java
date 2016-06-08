package com.ccmapper.simplecustom;

import java.util.Arrays;
import java.util.Collection;

import com.ccmapper.core.AbstractCCMapperBeanDefinitionRegistry;
import com.demo.bean.Demo;

public class SimpleBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry{

	@Override
	public Collection<Class<?>> getBeanList() {
		return Arrays.asList(Demo.class);
	}

	@Override
	public Class<?> getCommonMapper() {
		return SimpleCommonMapper.class;
	}

	@Override
	public Class<?> getSqlProvider() {
		return SimpleMapperSqlProvider.class;
	}

}
