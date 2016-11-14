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
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.ccmapper.core.example.Example;

/**
 * @Description: CustomCommonSqlProvider  一个简单的通用sql提供者
 * @author xiaoruihu 2016年11月14日 下午4:11:29
 */
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

	/**
	 * @Title: updateByPrimaryKey 
	 * @Description: 根据主键全量更新
	 * @author xiaoruihu
	 * @param o
	 * @return
	 */
	public String updateByPrimaryKey(Object o) {
		return updateAllByPrimaryKey(o, false);
	}

	/**
	 * @Title: updateNotNullByPrimaryKey 
	 * @Description: 根据主键只更新不为空的属性
	 * @author xiaoruihu
	 * @param o
	 * @return
	 */
	public String updateNotNullByPrimaryKey(Object o) {
		return updateAllByPrimaryKey(o, true);
	}
	
	/**
	 * @Title: updateByPrimary 
	 * @Description: 根据主键只更新指定属性
	 * @author xiaoruihu
	 * @param o
	 * @param updatePropertyList
	 * @return
	 */
	public String updateByPrimary(Object o, List<String> updatePropertyList){
		try {
			BEGIN();
			UPDATE(tableName);
			for (String propertyName : propertyAndColumnMap.keySet()) {
				String column = propertyAndColumnMap.get(propertyName);
				if (primaryKey.equals(propertyName)) {
					WHERE(column + "=#{" + primaryKey + "}");
				} else {
					if(updatePropertyList.contains(propertyName)){
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

	/**
	 * @Title: selectByPrimaryKey 
	 * @Description: 通过主键查找bean
	 * @author xiaoruihu
	 * @param primaryKey
	 * @return
	 */
	public String selectByPrimaryKey(Object primaryKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param1", "id");
		return selectByPropertyEqual(map);
	}

	/**
	 * @Title: selectAll 
	 * @Description: 查找所有
	 * @author xiaoruihu
	 * @return
	 */
	public String selectAll() {
		BEGIN();
		SELECT(allSelect(beanClazz));
		FROM(tableName);
		return SQL();
	}

	/**
	 * @Title: selectByPropertyEqual 
	 * @Description: 查找单个属性相等的
	 * @author xiaoruihu
	 * @param parameters
	 * @return
	 */
	public String selectByPropertyEqual(Map<String, Object> parameters) {
		String property = (String) parameters.get("param1");
		BEGIN();
		SELECT(allSelect(beanClazz));
		FROM(tableName);
		WHERE(propertyAndColumnMap.get(property) + "=#{param2}");
		return SQL();
	}

	/**
	 * @Title: selectListByExample 
	 * @Description: 动态sql
	 * @author xiaoruihu
	 * @param example
	 * @return
	 */
	public String selectListByExample(Example example) {
		BEGIN();
		String selectSql = example.generateSelectPropertiesSql(propertyAndColumnMap);
		if (selectSql == null) {
			SELECT(allSelect(beanClazz));
		} else {
			SELECT(selectSql);
		}
		FROM(tableName);

		String whereSql = example.generateWhereSql(propertyAndColumnMap);
		if (whereSql != null) {
			WHERE(whereSql);
		}

		String orderByString = example.generateOrderBysql(propertyAndColumnMap);
		if (orderByString != null) {
			ORDER_BY(orderByString);
		}

		return SQL();
	}

	/**
	 * @Title: allSelect 
	 * @Description: 获取所有的字段集合
	 * @author xiaoruihu
	 * @param clazz
	 * @return
	 */
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
