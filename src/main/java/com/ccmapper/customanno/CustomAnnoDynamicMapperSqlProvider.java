package com.ccmapper.customanno;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ccmapper.core.CustomCommonSqlProvider;

public class CustomAnnoDynamicMapperSqlProvider extends CustomCommonSqlProvider {

	private List<Method> readMethodList = new ArrayList<Method>();


	public CustomAnnoDynamicMapperSqlProvider(String className) {
		super(className);

		try {

			Table table = this.beanClazz.getAnnotation(Table.class);
			if (table == null) {
				throw new RuntimeException("改bean不存在Table注解  bean " + className);
			}

			this.tableName = table.name();

			if (this.tableName.equals("")) {
				this.tableName = this.beanClazz.getSimpleName();
			}

			Field[] fields = this.beanClazz.getDeclaredFields();

			for (Field f : fields) {

				if (f.isAnnotationPresent(Column.class)) {// 属于普通属性
					Column column = f.getAnnotation(Column.class);

					String columnName = null;
					if ("".equals(column.name())) {
						columnName = f.getName();
					} else {
						columnName = column.name();
					}

					propertyAndColumnMap.put(f.getName(), columnName);

				} else if (f.isAnnotationPresent(Id.class)) {// id属性
					propertyAndColumnMap.put(f.getName(), f.getName());
					this.primaryKey = f.getName();
				}
			}

			BeanInfo beanInfo = Introspector.getBeanInfo(this.beanClazz);
			PropertyDescriptor[] proDescs = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : proDescs) {
				if (propertyAndColumnMap.containsKey(pd.getName())) {
					readMethodList.add(pd.getReadMethod());
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("初始化bean属性失败 bean " + className, e);
		}

	}
	
}
