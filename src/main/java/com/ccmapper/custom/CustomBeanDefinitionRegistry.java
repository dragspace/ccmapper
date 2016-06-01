package com.ccmapper.custom;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.ccmapper.core.AbstractCCMapperBeanDefinitionRegistry;
import com.ccmapper.core.utils.MapperDynamicUtils;
import com.demo.bean.Demo4;

public class CustomBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry{
	
	private Class<?> defaultSqlProvider = CommonDynamicMapperProvider.class;
	private Class<?> defaultCommonMapper = CommonMapper.class;
	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		//MapperDynamicUtils.registerCommonMapper(Demo.class, registry, CommonMapper.class, CommonDynamicMapperProvider.class);
        MapperDynamicUtils.registerCommonMapper(Demo4.class, registry, defaultCommonMapper, defaultSqlProvider);
        
	}

	public Class<?> getDefaultSqlProvider() {
		return defaultSqlProvider;
	}

	public void setDefaultSqlProvider(Class<?> defaultSqlProvider) {
		this.defaultSqlProvider = defaultSqlProvider;
	}

	public Class<?> getDefaultCommonMapper() {
		return defaultCommonMapper;
	}

	public void setDefaultCommonMapper(Class<?> defaultCommonMapper) {
		this.defaultCommonMapper = defaultCommonMapper;
	}
}
