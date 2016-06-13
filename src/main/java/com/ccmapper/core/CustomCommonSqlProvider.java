package com.ccmapper.core;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.ccmapper.core.example.Example;

public class CustomCommonSqlProvider extends AbstractSqlProvider {

	protected Map<String, String> propertyAndColumnMap = new HashMap<String, String>();
	protected String tableName;
	protected String primaryKey;

	
	public CustomCommonSqlProvider(String className) {
		super(className);
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

	public String selectListByExample(Example example){
		BEGIN();
		String selectSql = example.generateSelectPropertiesSql(propertyAndColumnMap);
		if(selectSql == null){
			SELECT(allSelect(beanClazz));
		}else{
			SELECT(selectSql);
		}
		FROM(tableName);
		
		String whereSql = example.generateWhereSql(propertyAndColumnMap);
		if(whereSql != null){
			WHERE(whereSql);
		}
		
		String orderByString = example.generateOrderBysql(propertyAndColumnMap);
		if(orderByString != null){
			ORDER_BY(orderByString);
		}
		
		return SQL();
	}
	
	public String allSelect(Class<?> clazz) {
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
