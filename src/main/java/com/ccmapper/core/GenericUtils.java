package com.ccmapper.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.NotFoundException;

import com.ccmapper.custom.CommonMapper;


/**
 * @Description: GenericUtils 泛型工具类 主要是来处理 commmonmapper的泛型问题 以TCommonMapper为原型
 *               注意TCommonMapper一定要全部生成commonmapper的方法
 * @author xiaoruihu 2016年5月26日 下午4:02:54
 */
public class GenericUtils {

	public static CtClass tCommonMapperCtClass;
	public static CtClass commonMapperCtClass;
	public static ClassPool pool = ClassPool.getDefault();
	private static String tClassStr = TClass.class.getName().replace(".", "/");
	private static Map<String, CtMethod> tMapperMethodMap = new HashMap<String, CtMethod>();
	private static Map<String, Boolean> objectMethod = new HashMap<String, Boolean>();
	
	static {
		try {
			tCommonMapperCtClass = pool.getCtClass(TCommonMapper.class.getName());
			for (CtMethod ctMethod : tCommonMapperCtClass.getMethods()) {
				
				if(ctMethod.getGenericSignature() != null && ctMethod.getGenericSignature().endsWith("Ljava/lang/Object;")){
					continue;
				}
				
				tMapperMethodMap.put(ctMethod.getName(), ctMethod);
			}
			
			commonMapperCtClass = pool.getCtClass(CommonMapper.class.getName());
			
			for (Method ctMethod : Object.class.getMethods()) {
				objectMethod.put(ctMethod.getName(), true);
			}
			objectMethod.put("finalize", true);
			objectMethod.put("clone", true);

		} catch (Exception e) {
			// 。。。基本不可能
			throw new RuntimeException("初始化失败", e);
		}
	}
	
	public static boolean isObjectMethod(String shortName){
		return objectMethod.containsKey(shortName);
	}

	/**
	 * @Title: setTCommonMapperGeneric
	 * @Description: 设置mapper的class的泛型
	 * @author xiaoruihu
	 * @param beanClass
	 * @param mapperCtClass
	 */
	public static void setTCommonMapperGeneric(Class<?> beanClass, CtClass mapperCtClass) {
		String tGeneric = tCommonMapperCtClass.getGenericSignature();
		mapperCtClass.setGenericSignature(getGenericBeanClassString(tGeneric, beanClass));
	}

	/**
	 * @Title: setTMethodGeneric
	 * @Description: 设置方法泛型
	 * @author xiaoruihu
	 * @param beanClass
	 * @param ctMethod
	 */
	public static void setTMethodGeneric(Class<?> beanClass, CtMethod ctMethod) {
		try {

			// 设置方法返回值泛型
			String methodName = ctMethod.getName();
			CtMethod srcTMethod = tMapperMethodMap.get(methodName);
			if (srcTMethod == null) {
				throw new RuntimeException("不存在的方法[" + methodName + "] class [" + TCommonMapper.class.getName() + "]");
			}
			CtClass returnType = srcTMethod.getReturnType();
			if (returnType.getGenericSignature() != null) {
				ctMethod.getReturnType().setGenericSignature(getGenericBeanClassString(returnType.getGenericSignature(), beanClass));
			}
			// 设置方法泛型
			if (srcTMethod.getGenericSignature() != null) {
				ctMethod.setGenericSignature(getGenericBeanClassString(srcTMethod.getGenericSignature(), beanClass));
			}

		} catch (NotFoundException e) {
			throw new RuntimeException("设置方法泛型失败", e);
		}

	}

	private static String getGenericBeanClassString(String srcGeneric, Class<?> beanClass) {
		return srcGeneric.replace(tClassStr, beanClass.getName().replace(".", "/"));
	}

	public static Class<?> getSqlProvideClazz(Class<?> t){
		return null;
	}
}
