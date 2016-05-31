package com.ccmapper.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.NotFoundException;


/**
 * @Description: GenericUtils 泛型工具类 主要是来处理 commmonmapper的泛型问题 以TCommonMapper为原型
 *               注意TCommonMapper一定要全部生成commonmapper的方法
 * @author xiaoruihu 2016年5月26日 下午4:02:54
 */
public class GenericUtils {

	public static ClassPool pool = ClassPool.getDefault();
	private static Map<String, Boolean> objectMethod = new HashMap<String, Boolean>();
	
	static {
		try {
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

	public static CtClass getCtClass(Class<?> clazz){
		try {
			return pool.getCtClass(clazz.getName());
		} catch (NotFoundException e) {
			//不可能吧
			throw new RuntimeException("获取ctClass失败", e);
		}
	}
	
	
	/**
	 * @Title: setTCommonMapperGeneric
	 * @Description: 设置mapper的class的泛型
	 * @author xiaoruihu
	 * @param beanClass
	 * @param mapperCtClass
	 */
	public static void setTCommonMapperGeneric(Class<?> beanClass, CtClass mapperCtClass, Class<?> superMapperClass) {
		mapperCtClass.setGenericSignature(getMapperGenericString(superMapperClass.getName(), beanClass));
	}

	/**
	 * @Title: setTMethodGeneric
	 * @Description: 设置方法泛型
	 * @author xiaoruihu
	 * @param beanClass
	 * @param ctMethod
	 */
	public static void setTMethodGeneric(Class<?> beanClass, CtMethod ctMethod, CtClass superMapperClass) {
		try {

			// 设置方法返回值泛型
			String methodName = ctMethod.getName();
			CtMethod srcTMethod = getCtMethod(ctMethod.getName(), superMapperClass);
			if (srcTMethod == null) {
				throw new RuntimeException("不存在的方法[" + methodName + "]");
			}
			// 设置方法泛型
			if (srcTMethod.getGenericSignature() != null) {
				ctMethod.setGenericSignature(getGenericBeanClassString(srcTMethod.getGenericSignature(), beanClass));
			}

		} catch (Exception e) {
			throw new RuntimeException("设置方法泛型失败", e);
		}
	}
	
	private static CtMethod getCtMethod(String methdoName, CtClass ctc){
		
		for(CtMethod cm : ctc.getMethods()){
			if(methdoName.equals(cm.getName())){
				return cm;
			}
		}
		throw new RuntimeException(String.format("没有找到该方法%s, 类 %s", methdoName, ctc.getName()));
	} 

	public static String getMapperGenericString(String superClassName, Class<?> beanClass){
		//Ljava/lang/Object;Lcom/ccmapper/custom/CommonMapper<Lcom/ccmapper/core/TClass;>;
		return String.format("Ljava/lang/Object;L%s<L%s;>;", superClassName.replace(".", "/"), beanClass.getName().replace(".", "/"));
		
	}
	
	
	private static String getGenericBeanClassString(String srcGeneric, Class<?> beanClass) {
		return srcGeneric.replace("TT", "L" + beanClass.getName().replace(".", "/"));
	}

}
