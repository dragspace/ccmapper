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

public class ScannerUtils {

	private static final String RESOURCE_PATTERN = "/**/*.class";

	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * @Title: getClassSetAnnotation
	 * @Description: 将符合条件的class 注解
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
	 * @Description: 将符合条件的class 子类
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
	 * @Description: 获取指定报下所有的类
	 * @author xiaoruihu
	 * @param basePackage
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String basePackage) {
		return getClassSet(basePackage, null);
	}

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
