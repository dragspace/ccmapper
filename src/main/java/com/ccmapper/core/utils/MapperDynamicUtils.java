package com.ccmapper.core.utils;

import java.util.ArrayList;
import java.util.List;

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


public class MapperDynamicUtils {
	
	private static final String PRE_MAPPER_PACKAGE_NAME = "com.ccmapper.mapper.dynamic.";
	private static final String PRE_SQLPROVIDER_PACKAGE_NAME = "com.ccmapper.sqlprovider.dynamic.";

	/**
	 * 需要修改的mybatis注解
	 */
	private static List<Class<?>> mybatisProviderList;
	static{
		//Arrays.asList(SelectProvider.class, UpdateProvider.class, InsertProvider.class, DeleteProvider.class);
		mybatisProviderList = new ArrayList<Class<?>>();
		mybatisProviderList.add(SelectProvider.class);
		mybatisProviderList.add(UpdateProvider.class);
		mybatisProviderList.add(InsertProvider.class);
		mybatisProviderList.add(DeleteProvider.class);
		
		
	}
	/**
	 * @Title: generateMapperClass
	 * @Description: 生成代理接口类
	 * @author xiaoruihu
	 * @param beanClazz
	 * @param commonMapperClass
	 * @return
	 */
	public static Class<?> generateMapperClass(Class<?> beanClazz, Class<?> commonMapperClass, Class<?> commonSqlProviderClass) {

		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(MapperDynamicUtils.class));
			CtClass superIn = pool.get(commonMapperClass.getName());
			CtClass ct = pool.makeInterface(PRE_MAPPER_PACKAGE_NAME + "Proxy" + commonMapperClass.getSimpleName() + beanClazz.getSimpleName());
			GenericUtils.setTCommonMapperGeneric(beanClazz, ct, commonMapperClass);
			modifyAnnotation(ct, beanClazz,commonMapperClass, commonSqlProviderClass, superIn);
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
	private static void modifyAnnotation(CtClass ctChild, Class<?> beanClazz, Class<?> commonMapperClass, Class<?> commonSqlProviderClass, CtClass superMapperCtClass) {

		CtMethod[] ctMethods = GenericUtils.getCtClass(commonMapperClass).getMethods();
		Class<?> customCommonSqlProviderClass = generateSqlProvider(beanClazz, commonSqlProviderClass);
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
				GenericUtils.setTMethodGeneric(beanClazz, newMethod, superMapperCtClass);
				AnnotationsAttribute attribute = (AnnotationsAttribute) minfo.getAttribute(AnnotationsAttribute.visibleTag);
				//这种的添加方法
				
				if (attribute != null) {
					for (Class<?> annoClass : mybatisProviderList) {
						Annotation anno = attribute.getAnnotation(annoClass.getName());
						if (anno != null) {
							ConstPool cp2 = newMethod.getMethodInfo().getConstPool();
							AnnotationsAttribute newAttribute = new AnnotationsAttribute(cp2, AnnotationsAttribute.visibleTag);
							modifySqlProviderAnnotation(newMethod, newAttribute, anno, commonSqlProviderClass, customCommonSqlProviderClass, annoClass);
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
	private static void modifySqlProviderAnnotation(CtMethod newMethod, AnnotationsAttribute newAnnoAttr, Annotation anno, Class<?> commonSqlProviderClass,Class<?> customerCommonSqlProviderClass,
			Class<?> annoClass) {
		if (anno == null) {
			return;
		}
		ClassMemberValue cmv = (ClassMemberValue) anno.getMemberValue("type");
		// 判断是否为需要换取的注解
		if (commonSqlProviderClass.getName().equals(cmv.getValue())) {
			StringMemberValue methodStr = (StringMemberValue) anno.getMemberValue("method");
			MethodInfo newMethodInfo = newMethod.getMethodInfo();
			ConstPool cp2 = newMethodInfo.getConstPool();
			Annotation annotation = new Annotation(annoClass.getName(), cp2);
			annotation.addMemberValue("method", new StringMemberValue(methodStr.getValue(), cp2));
			annotation.addMemberValue("type", new ClassMemberValue(customerCommonSqlProviderClass.getName(), cp2));
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
	private static Class<?> generateSqlProvider(Class<?> beanClass, Class<?> superSqlProviderClass) {
		try {
			Class<?> superClass = superSqlProviderClass;
			String genString = "<L" + beanClass.getName().replace(".", "/") + ";>";
			String beseString = "L" + superClass.getName().replace(".", "/");
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(MapperDynamicUtils.class));
			CtClass superIn = pool.get(superClass.getName());
			CtClass ct = pool.makeClass(PRE_SQLPROVIDER_PACKAGE_NAME + "Proxy" + superSqlProviderClass.getSimpleName() + beanClass.getSimpleName());
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

//	private static String generateClassName(String pre) {
//		return "com.dynamic." + pre;
//	}

	public static void registerCommonMapper(Class<?> beanClazz, BeanDefinitionRegistry registry, Class<?> commonMapperClass, Class<?> commonSqlProviderClass) {

		Class<?> clazz = generateMapperClass(beanClazz, commonMapperClass, commonSqlProviderClass);
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
