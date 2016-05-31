package com.ccmapper.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanAndTableUtils {
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
}
