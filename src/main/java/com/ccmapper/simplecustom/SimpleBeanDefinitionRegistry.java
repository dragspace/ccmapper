package com.ccmapper.simplecustom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ccmapper.core.AbstractCCMapperBeanDefinitionRegistry;
import com.demo.bean.Demo;

public class SimpleBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry{

	@Override
	public Collection<Class<?>> getBeanList() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Demo.class);
		return list;
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
