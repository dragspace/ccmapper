package com.ccmapper.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @Description: AbstractCCMapperBeanDefinitionRegistry  集成这个类来说实现
 * @author xiaoruihu 2016年6月1日 上午11:21:23
 */
public abstract class AbstractCCMapperBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor{
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}
	
}
