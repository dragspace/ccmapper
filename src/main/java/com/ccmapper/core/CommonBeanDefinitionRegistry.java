package com.ccmapper.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ccmapper.core.utils.ScannerUtils;

public class CommonBeanDefinitionRegistry extends AbstractCCMapperBeanDefinitionRegistry {

	/**
	 * 基础bean的路径
	 */
	private String baseBeanPackage;
	
	/**
	 * 自定义bean路径
	 */
	private List<Class<?>> beanClasssList;

	private Class<?> commonMapper;
	private Class<?> sqlProvider;

	@Override
	public Collection<Class<?>> getBeanList() {
		Set<Class<?>> beanSet = new HashSet<Class<?>>();
		if (baseBeanPackage != null) {
			beanSet.addAll(ScannerUtils.getClassSet(baseBeanPackage));
		}

		if (beanClasssList != null) {
			beanSet.addAll(beanClasssList);
//			try {
//				for (String className : beanClasssNameList) {
//					beanSet.add(Class.forName(className));
//				}
//			} catch (ClassNotFoundException e) {
//				throw new IllegalArgumentException("beanClasssNameList注入失败", e);
//			}
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

}
