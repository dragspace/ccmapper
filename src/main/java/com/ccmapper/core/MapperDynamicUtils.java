package com.ccmapper.core;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtConstructor;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.bytecode.annotation.Annotation;
import org.apache.ibatis.javassist.bytecode.annotation.ClassMemberValue;
import org.apache.ibatis.javassist.bytecode.annotation.StringMemberValue;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.ccmapper.custom.CommonDynamicMapperProvider;
import com.ccmapper.custom.CommonMapper;


public class MapperDynamicUtils {

	/**
	 * 需要修改的mybatis注解
	 */
	private static List<Class<?>> mybatisProviderList = Arrays.asList(SelectProvider.class, UpdateProvider.class, InsertProvider.class, DeleteProvider.class);

	/**
	 * @Title: getAllPropertyAndColumnMap
	 * @Description: 简单获取属性和字段映射
	 * @author xiaoruihu
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getAllPropertyAndColumnMap(Class<?> clazz) {

		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = clazz.getDeclaredFields();

		for (Field f : fields) {
			String fileName = f.getName();
			if ("serialVersionUID".equals(fileName)) {
				continue;
			}
			map.put(fileName, getColumnName(false, fileName));
		}
		return map;
	}

	/**
	 * @Title: getColumnName
	 * @Description: TODOo
	 * @author xiaoruihu
	 * @param flag
	 *            true字段 将驼峰改成下划线 false 字段和属性相同
	 */
	private static String getColumnName(boolean flag, String str) {

		if (flag) {
			StringBuilder sb = new StringBuilder();
			String[] ss = str.split("(?=[A-Z])");
			for (String s : ss) {
				sb.append(s.toLowerCase() + "_");
			}
			return sb.deleteCharAt(sb.length() - 1).toString();
		} else {
			return str;
		}
	}

	/**
	 * @Title: generateMapperClass
	 * @Description: 生成代理接口类
	 * @author xiaoruihu
	 * @param beanClazz
	 * @param CommonMapperClass
	 * @return
	 */
	public static Class<?> generateMapperClass(Class<?> beanClazz, Class<?> CommonMapperClass) {

		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(MapperDynamicUtils.class));
			CtClass superIn = pool.get(CommonMapperClass.getName());
			CtClass ct = pool.makeInterface(generateClassName("ProxyCommonMapper" + beanClazz.getSimpleName()));
			GenericUtils.setTCommonMapperGeneric(beanClazz, ct);
			modifyAnnotation(ct, beanClazz);
			ct.setSuperclass(superIn);
			return ct.toClass();
		} catch (Exception e) {
			throw new RuntimeException("动态生成接口失败", e);
		}
	}

	/**
	 * @Title: modifyAnnotation
	 * @Description: 修改mybatis的注解参数的type为自定义的class
	 * @author xiaoruihu
	 * @param ct
	 */
	private static void modifyAnnotation(CtClass ctChild, Class<?> beanClazz) {

		CtMethod[] ctMethods = GenericUtils.commonMapperCtClass.getMethods();
		String commonSqlProviderClassName = generateSqlProvider(beanClazz).getName();
		for (CtMethod ctMethod : ctMethods) {
			if (GenericUtils.isObjectMethod(ctMethod.getName())) {
				continue;
			}
			try {
				
				CtClass resultType = ctMethod.getReturnType();
				if(resultType.getName().equals(Object.class.getName())){
					resultType =  GenericUtils.pool.get(beanClazz.getName());
					//同时构造一个方法
					ctChild.addMethod(new CtMethod(ctMethod.getReturnType(), ctMethod.getName(), ctMethod.getParameterTypes(), ctChild));
				}
				
				MethodInfo minfo = ctMethod.getMethodInfo();
				CtMethod newMethod = new CtMethod(resultType, ctMethod.getName(), ctMethod.getParameterTypes(), ctChild);
				GenericUtils.setTMethodGeneric(beanClazz, newMethod);
				AnnotationsAttribute attribute = (AnnotationsAttribute) minfo.getAttribute(AnnotationsAttribute.visibleTag);
				//这种的添加方法
				
				if (attribute != null) {
					for (Class<?> annoClass : mybatisProviderList) {
						Annotation anno = attribute.getAnnotation(annoClass.getName());
						if (anno != null) {
							GenericUtils.setTMethodGeneric(beanClazz, newMethod);
							ConstPool cp2 = newMethod.getMethodInfo().getConstPool();
							AnnotationsAttribute newAttribute = new AnnotationsAttribute(cp2, AnnotationsAttribute.visibleTag);
							modifySqlProviderAnnotation(newMethod, newAttribute, anno, commonSqlProviderClassName, annoClass);
							newMethod.getMethodInfo().addAttribute(newAttribute);
							break;
						} 
					}
				}
				ctChild.addMethod(newMethod);
			} catch (Exception e) {
				throw new RuntimeException("动态生成mapp添加方法出现异常", e);
			}

		}

	}
	

	/**
	 * @Title: modifySqlProviderAnnotation
	 * @Description: 修改4个增删改的sqlprovider
	 * @author xiaoruihu
	 * @param ctMethod
	 * @param anno
	 * @param commonSqlProviderClassName
	 * @param ctChild
	 * @param annoClass
	 */
	private static void modifySqlProviderAnnotation(CtMethod newMethod, AnnotationsAttribute newAnnoAttr, Annotation anno, String commonSqlProviderClassName,
			Class<?> annoClass) {
		if (anno == null) {
			return;
		}
		ClassMemberValue cmv = (ClassMemberValue) anno.getMemberValue("type");
		// 判断是否为需要换取的注解
		if (CommonDynamicMapperProvider.class.getName().equals(cmv.getValue())) {
			StringMemberValue methodStr = (StringMemberValue) anno.getMemberValue("method");
			MethodInfo newMethodInfo = newMethod.getMethodInfo();
			ConstPool cp2 = newMethodInfo.getConstPool();
			Annotation annotation = new Annotation(annoClass.getName(), cp2);
			annotation.addMemberValue("method", new StringMemberValue(methodStr.getValue(), cp2));
			annotation.addMemberValue("type", new ClassMemberValue(commonSqlProviderClassName, cp2));
			newAnnoAttr.addAnnotation(annotation);
		}
	}

	/**
	 * @Title: generateSqlProvider
	 * @Description: 生成代理的sql提供者类
	 * @author xiaoruihu
	 * @param beanClass
	 * @return
	 */
	private static Class<?> generateSqlProvider(Class<?> beanClass) {
		try {
			Class<?> superClass = CommonDynamicMapperProvider.class;
			String genString = "<L" + beanClass.getName().replace(".", "/") + ";>";
			String beseString = "L" + superClass.getName().replace(".", "/");
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(MapperDynamicUtils.class));
			CtClass superIn = pool.get(superClass.getName());
			CtClass ct = pool.makeClass(generateClassName("ProxyCommonMapperProvider" + beanClass.getSimpleName()));
			ct.setGenericSignature(beseString + genString + ";");
			ct.setSuperclass(superIn);
			CtConstructor cons = new CtConstructor(new CtClass[] {}, ct);
			String className = beanClass.getName();
			cons.setBody(String.format("{super(%s);}", "\"" + className + "\""));
			ct.addConstructor(cons);

			return ct.toClass();
		} catch (Exception e) {
			throw new RuntimeException("动态生成类失败", e);
		}
	}

	private static String generateClassName(String pre) {
		return "com.dynamic." + pre;
	}

	public static void registerCommonMapper(Class<?> beanClazz, BeanDefinitionRegistry registry) {

		Class<?> clazz = generateMapperClass(beanClazz, CommonMapper.class);
		// 通过BeanDefinitionBuilder创建bean定义
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		// 注册bean
		String beanname = clazz.getSimpleName();
		BeanDefinition definition = beanDefinitionBuilder.getRawBeanDefinition();
		definition.setBeanClassName(MapperFactoryBean.class.getName());
		definition.getPropertyValues().add("addToConfig", true);
		definition.getPropertyValues().add("mapperInterface", clazz);
		definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference("sqlSessionFactory"));
		registry.registerBeanDefinition(beanname, definition);
	}

}
