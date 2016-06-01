package com.ccmapper.core.utils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;


/**
 * @Description: ScannerUtils  一个扫描工具类
 * @author xiaoruihu 2016年6月1日 上午11:27:15
 */
public class ScannerUtils {

	/**
	 * 默认包递归
	 */
	private static final String RESOURCE_PATTERN = "/**/*.class";

	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * @Title: getClassSetAnnotation
	 * @Description: 获取拥有指定注解的class
	 * @author xiaoruihu
	 * @param basePackage
	 * @param annotation
	 * @return
	 */
	public static Set<Class<?>> getClassSetAnnotation(String basePackage, Class<? extends Annotation> annotation) {
		return getClassSet(basePackage, new AnnotationTypeFilter(annotation, false));
	}

	/**
	 * @Title: getClassSetSuperClass
	 * @Description: 获取拥有指定父类或者父接口的 子类 (本方法自动去除父类)
	 * @author xiaoruihu
	 * @param basePackage
	 * @param superClazz
	 * @return
	 */
	public static Set<Class<?>> getClassSetSuperClass(String basePackage, Class<?> superClazz) {
		Set<Class<?>> set = getClassSet(basePackage, new AssignableTypeFilter(superClazz));
		set.remove(superClazz);
		return set;
	}

	/**
	 * @Title: getClassSet
	 * @Description: 获取指定包下的所有类 
	 * @author xiaoruihu
	 * @param basePackage
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String basePackage) {
		return getClassSet(basePackage, null);
	}

	/**
	 * @Title: getClassSet 
	 * @Description: 注意这个是包递归
	 * @author xiaoruihu
	 * @param basePackage
	 * @param tf
	 * @return
	 */
	private static Set<Class<?>> getClassSet(String basePackage, TypeFilter tf) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(pattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader reader = readerFactory.getMetadataReader(resource);
					String className = reader.getClassMetadata().getClassName();
					if (tf == null) {
						classSet.add(Class.forName(className));
					} else {
						if (tf.match(reader, readerFactory)) {
							classSet.add(Class.forName(className));
						}
					}
				}
			}
			return classSet;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
