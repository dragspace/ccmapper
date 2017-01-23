package com.ccmapper.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ccmapper.core.utils.ScannerUtils;

/**
 * @Description: CommonBeanDefinitionRegistry 方便通用mapper注册
 * @author xiaoruihu 2016年11月14日 下午4:08:46
 */
public class CommonBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry {

	/**
	 * 基础bean的 **包**  路径
	 */
	private String baseBeanPackage;

	/**
	 * 自定义单个bean的**class**路径
	 */
	private List<Class<?>> beanClasssList;

	private Class<?> commonMapper;
	
	private Class<?> sqlProvider;
	
	/**
	 * sqlSessionFactory的ID
	 */
	private String sqlSessionFactoryBeanName;

	@Override
	public Collection<Class<?>> getBeanList() {
		Set<Class<?>> beanSet = new HashSet<Class<?>>();
		if (baseBeanPackage != null) {
			beanSet.addAll(ScannerUtils.getClassSet(baseBeanPackage));
		}

		if (beanClasssList != null) {
			beanSet.addAll(beanClasssList);
		}

		return beanSet;
	}

	@Override
	public Class<?> getCommonMapper() {
		return commonMapper;
	}

	@Override
	public Class<?> getSqlProvider() {
		return sqlProvider;
	}

	@Override
	public String getSqlSessionFactoryBeanName() {
		return this.sqlSessionFactoryBeanName;
	}
	
	public void setBaseBeanPackage(String baseBeanPackage) {
		this.baseBeanPackage = baseBeanPackage;
	}

	public void setBeanClasssList(List<Class<?>> beanClasssList) {
		this.beanClasssList = beanClasssList;
	}

	public void setCommonMapper(Class<?> commonMapper) {
		this.commonMapper = commonMapper;
	}

	public void setSqlProvider(Class<?> sqlProvider) {
		this.sqlProvider = sqlProvider;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
	}

}
