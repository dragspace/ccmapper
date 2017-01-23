package com.ccmapper.core;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.CollectionUtils;

import com.ccmapper.core.utils.MapperDynamicUtils;

/**
 * @Description: AbstractCCMapperBeanDefinitionRegistry 集成这个类来指定类实现
 * @author xiaoruihu 2016年6月1日 上午11:21:23
 */
public abstract class AbstractCCMapperBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

		Collection<Class<?>> beanClassCollection = this.getBeanList();
		if (CollectionUtils.isEmpty(beanClassCollection)) {
			return;
		}

		if (getCommonMapper() == null || getSqlProvider() == null) {
			throw new NullPointerException(this.getClass().getName() + "的beanClasssName和sqlProvider 不能为空");
		}
		for (Class<?> clazz : beanClassCollection) {
			MapperDynamicUtils.registerCommonMapper(clazz, registry, getCommonMapper(), getSqlProvider(), getSqlSessionFactoryBeanName());
		}
	}

	/**
	 * @Title: getBeanList
	 * @Description: 获取要注册beanclass
	 * @author xiaoruihu
	 * @return
	 */
	public abstract Collection<Class<?>> getBeanList();

	/**
	 * @Title: getCommonMapper
	 * @Description: 获取通用mapper
	 * @author xiaoruihu
	 * @return
	 */
	public abstract Class<?> getCommonMapper();

	/**
	 * @Title: getSqlProvider
	 * @Description: 获取sql提供者
	 * @author xiaoruihu
	 * @param <?>
	 *            T extends AbstractSqlProvider
	 * @return
	 */
	public abstract Class<?> getSqlProvider();
	
	
	public abstract String getSqlSessionFactoryBeanName();

}
