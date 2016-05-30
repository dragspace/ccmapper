package com.ccmapper.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import com.demo.bean.Demo;
import com.demo.bean.Demo4;

public class CCMapperBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor{

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		MapperDynamicUtils.registerCommonMapper(Demo.class, registry);
        MapperDynamicUtils.registerCommonMapper(Demo4.class, registry);
	}

}
