package com.ccmapper.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import com.ccmapper.custom.CommonDynamicMapperProvider;
import com.ccmapper.custom.CommonMapper;
import com.ccmapper.utils.MapperDynamicUtils;
import com.demo.bean.Demo4;

public class CCMapperBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor{

	private Class<?> defaultSqlProvider = CommonDynamicMapperProvider.class;
	private Class<?> defaultCommonMapper = CommonMapper.class;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

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
