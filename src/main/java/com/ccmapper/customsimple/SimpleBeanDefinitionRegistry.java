package com.ccmapper.customsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ccmapper.core.AbstractCCMapperBeanDefinitionRegistry;
import com.demo.simple.SimpleBean;

public class SimpleBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry{

	@Override
	public Collection<Class<?>> getBeanList() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(SimpleBean.class);
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

	@Override
	public String getSqlSessionFactoryBeanName() {
		return "sqlSessionFactory";
	}

	@Override
	public String getBeannamePrefix() {
		return "simpleBeanNamePrefix";
	}
}
