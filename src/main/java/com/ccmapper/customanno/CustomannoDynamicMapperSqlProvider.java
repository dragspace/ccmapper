package com.ccmapper.customanno;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.ccmapper.core.AbstractSqlProvider;

public class CustomannoDynamicMapperSqlProvider extends AbstractSqlProvider {

	private List<Method> readMethodList = new ArrayList<Method>();
	private Map<String, String> propertyAndColumnMap = new HashMap<String, String>();
	private String primaryKey;

	private String tableName;

	public CustomannoDynamicMapperSqlProvider(String className) {
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

	public String insert(Object o) {
		BEGIN();
		INSERT_INTO(tableName);
		for (String propertyName : propertyAndColumnMap.keySet()) {
			VALUES(propertyAndColumnMap.get(propertyName), "#{" + propertyName + "}");
		}
		return SQL();
	}

	public String updateByPrimaryKey(Object o) {
		return updateAllByPrimaryKey(o, false);
	}

	public String updateNotNullByPrimaryKey(Object o) {
		return updateAllByPrimaryKey(o, true);
	}

	private String updateAllByPrimaryKey(Object o, boolean isNotNull) {
		try {
			BEGIN();
			UPDATE(tableName);
			for (String propertyName : propertyAndColumnMap.keySet()) {
				String column = propertyAndColumnMap.get(propertyName);
				if (primaryKey.equals(propertyName)) {
					WHERE(column + "=#{" + primaryKey + "}");
				} else {
					if (isNotNull) {
						PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(beanClazz, propertyName);
						Method getMethd = pd.getReadMethod();
						Object value = getMethd.invoke(o);
						if (value != null) {
							if (value instanceof String) {
								if (!StringUtils.isEmpty(value)) {
									SET(column + "=#{" + propertyName + "}");
								}
							} else {
								SET(column + "=#{" + propertyName + "}");
							}
						}
					} else {
						SET(column + "=#{" + propertyName + "}");
					}
				}
			}
		} catch (Exception e) {
			// 抛出SQLException。。。还有异常
			throw new RuntimeException("拼接sql出现未知异常" + o, e);
		}
		return SQL();
	}

	public String selectByPrimaryKey(Object primaryKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param1", "id");
		return selectByPropertyEqual(map);
	}

	public String selectAll() {
		BEGIN();
		SELECT(allSelect(beanClazz));
		FROM(tableName);
		return SQL();
	}

	public String selectByPropertyEqual(Map<String, Object> parameters) {
		String property = (String) parameters.get("param1");
		BEGIN();
		SELECT(allSelect(beanClazz));
		FROM(tableName);
		WHERE(propertyAndColumnMap.get(property) + "=#{param2}");
		return SQL();
	}

	private String allSelect(Class<?> clazz) {
		StringBuilder selectSB = new StringBuilder();
		for (String property : propertyAndColumnMap.keySet()) {
			selectSB.append(propertyAndColumnMap.get(property));
			selectSB.append(" as ");
			selectSB.append(property);
			selectSB.append(",");
		}
		selectSB.deleteCharAt(selectSB.length() - 1);

		return selectSB.toString();
	}
}
