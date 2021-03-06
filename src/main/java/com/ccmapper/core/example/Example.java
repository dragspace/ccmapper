/*
 * The MIT License (MIT) Copyright (c) 2014-2016 abel533@gmail.com Permission is
 * hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions: The above copyright notice and this
 * permission notice shall be included in all copies or substantial portions of
 * the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.ccmapper.core.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccmapper.core.utils.CCStringUtils;

/**
 * 通用的Example查询对象
 */
public class Example extends HashMap<String, Object> {

	// private boolean distinct;

	/**
	 * @Fields serialVersionUID : serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected boolean notNull;

	private List<String> selectPropertyList;

	// private List<Criteria> orCriteriaList;

	private List<Criteria> criteriaList;

	private List<OrderBy> orderByList;

	private List<Object> params = new ArrayList<Object>();

	/**
	 * 带exists参数的构造方法
	 * 
	 * @param notNull
	 *            - true时，如果值为空，就会抛出异常，false时，如果为空就不使用该字段的条件
	 */
	public Example(boolean notNull) {
		this.notNull = notNull;
		this.criteriaList = new ArrayList<Criteria>();
		this.orderByList = new ArrayList<OrderBy>();
	}

	public Example() {
		this(false);
	}

	public OrderBy orderBy(String propertyName) {
		OrderBy ob = new OrderBy(propertyName);
		this.orderByList.add(ob);
		return ob;
	}

	public static class OrderBy {

		private static final String ASC = "  ASC";

		private static final String DESC = "  DESC";

		private String propertyName;

		private String ascOrDesc;

		public OrderBy(String propertyName) {
			this.propertyName = propertyName;
			ascOrDesc = ASC;
		}

		public OrderBy desc() {
			this.ascOrDesc = DESC;
			return this;
		}

		public OrderBy asc() {
			this.ascOrDesc = ASC;
			return this;
		}

		/**
		 * @Title: generateOrderSql
		 * @Description: 生成排序orderBy
		 * @author xiaoruihu
		 * @param aliasTableName
		 * @return
		 */
		protected String generateOrderSql(Map<String, String> propertyAndColumnMap) {
			return propertyAndColumnMap.get(this.propertyName) + this.ascOrDesc;
		}
	}

	/**
	 * @Title: selectProperties
	 * @Description: 指定要查询的属性列 - 这一这里不要指定重复的属性 你懂得。。。不想用set
	 * @author xiaoruihu
	 * @param properties
	 * @return
	 */
	public Example selectProperties(String... properties) {
		if (properties != null && properties.length > 0) {
			this.selectPropertyList = Arrays.asList(properties);
		}
		return this;
	}

	/**
	 * @Title: andCriteria
	 * @Description: 这个只会返回 唯一一个实例
	 * @author xiaoruihu
	 * @return
	 */
	public Criteria createAndCriteria() {
		Criteria criteria = new Criteria(notNull, ExampleConstant.AND);
		this.criteriaList.add(criteria);
		return criteria;
	}
	
	/**
	 * @Title: andCriteria
	 * @Description: 这个只会返回 唯一一个实例
	 * @author xiaoruihu
	 * @return
	 */
	public Criteria createOrCriteria() {
		Criteria criteria = new Criteria(notNull, ExampleConstant.OR);
		this.criteriaList.add(criteria);
		return criteria;
	}

	public String generateWhereSql(Map<String, String> propertyAndColumnMap) {

		StringBuilder whereSqlSB = new StringBuilder();

		for (Criteria c : this.criteriaList) {

			String criteriaSql = c.generateSql(propertyAndColumnMap, params);
			if (criteriaSql == null) {
				continue;
			}
			
			if (c.isMany()) {
				whereSqlSB.append("(" + criteriaSql + ")");
			} else {
				whereSqlSB.append(criteriaSql);
			}
			whereSqlSB.append(ExampleConstant.AND);
		}
		CCStringUtils.deleteEnd(whereSqlSB, ExampleConstant.AND);

		String whereSql = whereSqlSB.toString();
		if ("".equals(whereSql)) {
			whereSql = null;
		}

		return whereSql;
	}

	public String generateOrderBysql(Map<String, String> propertyAndColumnMap) {

		if (!this.orderByList.isEmpty()) {
			StringBuilder orderBySqlSB = new StringBuilder();

			for (OrderBy orderBy : this.orderByList) {
				orderBySqlSB.append(orderBy.generateOrderSql(propertyAndColumnMap));
				orderBySqlSB.append(",");
			}
			CCStringUtils.deleteEnd(orderBySqlSB, ",");
			return orderBySqlSB.toString();
		}
		return null;
	}

	public String generateSelectPropertiesSql(Map<String, String> propertyAndColumnMap) {
		if (this.selectPropertyList == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for (String propertyName : this.selectPropertyList) {
				sb.append(propertyAndColumnMap.get(propertyName));
				sb.append(", ");
			}
			return CCStringUtils.deleteEnd(sb, ", ").toString();
		}
	}

	@Override
	public Object get(Object key) {
		return this.params.get(Integer.parseInt(key.toString()));
	}

	public static class Criteria {

		private List<Criterion> criterionList;

		// 值是否不能为空
		protected boolean notNull;
		
		private String andOrOr;

		protected Criteria(boolean notNull, String andOrOr) {
			this.notNull = notNull;
			criterionList = new ArrayList<Criterion>();
			this.andOrOr = andOrOr;
		}

		protected boolean isMany() {
			return criterionList.size() > 1;
		}

		protected String generateSql(Map<String, String> propertyAndColumnMap, List<Object> params) {
			StringBuilder sb = new StringBuilder();
			if (this.criterionList.isEmpty()) {
				return null;
			}
			for (Criterion c : this.criterionList) {
				sb.append(c.generateSql(propertyAndColumnMap, params));
				sb.append(this.andOrOr);
			}

			return CCStringUtils.deleteEnd(sb, this.andOrOr).toString();
		}

		protected void addCriterion(SqlSign sqlSign, String propertyName) {
			if (propertyName == null) {
				return;
			}
			criterionList.add(new Criterion(sqlSign, propertyName));
		}

		protected void addCriterion(SqlSign sqlSign, String propertyName, Object value) {
			if (value == null) {
				if (notNull) {
					throw new RuntimeException("Value for " + value + " cannot be null");
				} else {
					return;
				}
			}
			if (propertyName == null) {
				return;
			}
			criterionList.add(new Criterion(sqlSign, propertyName, value));
		}

		protected void addCriterion(SqlSign sqlSign, String propertyName, Object value1, Object value2) {
			if (value1 == null || value2 == null) {
				if (notNull) {
					throw new RuntimeException("Between values for " + propertyName + " cannot be null");
				} else {
					return;
				}
			}
			if (propertyName == null) {
				return;
			}
			criterionList.add(new Criterion(sqlSign, propertyName, value1, value2));
		}

		public Criteria andIsNull(String propertyName) {
			addCriterion(SqlSign.IsNull, propertyName);
			return this;
		}

		public Criteria andIsNotNull(String propertyName) {
			addCriterion(SqlSign.IsNotNull, propertyName);
			return this;
		}

		public Criteria equalTo(String property, Object value) {
			return andSqlSign(SqlSign.EqualTo, property, value);
		}

		public Criteria notEqualTo(String property, Object value) {
			return andSqlSign(SqlSign.NotEqualTo, property, value);
		}

		public Criteria greaterThan(String property, Object value) {
			return andSqlSign(SqlSign.GreaterThan, property, value);
		}

		public Criteria greaterThanOrEqualTo(String property, Object value) {
			return andSqlSign(SqlSign.GreaterThanOrEqualTo, property, value);
		}

		public Criteria lessThan(String property, Object value) {
			return andSqlSign(SqlSign.LessThan, property, value);
		}

		public Criteria lessThanOrEqualTo(String property, Object value) {
			return andSqlSign(SqlSign.LessThanOrEqualTo, property, value);
		}

		public Criteria in(String property, Collection<?> values) {
			return andSqlSign(SqlSign.In, property, values);
		}
		
		public Criteria in(String property, Object... values) {
			return andSqlSign(SqlSign.In, property, Arrays.asList(values));
		}

		public Criteria notIn(String property, Collection<?> values) {
			return andSqlSign(SqlSign.NotIn, property, values);
		}
		
		public Criteria notIn(String property, Object... values) {
			return andSqlSign(SqlSign.NotIn, property, Arrays.asList(values));
		}

		public Criteria between(String property, Object value1, Object value2) {
			addCriterion(SqlSign.Between, property, value1, value2);
			return this;
		}

		public Criteria notBetween(String property, Object value1, Object value2) {
			addCriterion(SqlSign.NotBetween, property, value1, value2);
			return this;
		}

		public Criteria like(String property, String value) {
			return andSqlSign(SqlSign.Like, property, value);
		}

		public Criteria notLike(String property, String value) {
			return andSqlSign(SqlSign.NotLike, property, value);
		}

		private Criteria andSqlSign(SqlSign sqlSign, String propertyName, Object value) {
			addCriterion(sqlSign, propertyName, value);
			return this;
		}

	}

	/**
	 * @Description: Criterion 这里为了简化代码使用boolean 来区分各种条件。。。
	 * @author xiaoruihu 2016年6月6日 下午3:47:04
	 */
	protected static class Criterion {

		private SqlSign sqlSign;

		private String propertyName;

		private Object value;

		private Object secondValue;

		private boolean isCollection = false;

		protected Criterion(SqlSign sqlSign, String propertyName) {
			this.propertyName = propertyName;
			this.sqlSign = sqlSign;
		}

		protected Criterion(SqlSign sqlSign, String propertyName, Object value) {
			this(sqlSign, propertyName);
			this.value = value;

			if (value instanceof Collection) {
				isCollection = true;
			}
		}

		protected Criterion(SqlSign sqlSign, String propertyName, Object value, Object secondValue) {
			this(sqlSign, propertyName);
			this.value = value;
			this.secondValue = secondValue;
		}

		protected String generateSql(Map<String, String> propertyAndColumnMap, List<Object> params) {

			int index = params.size();
			if (isCollection) {
				params.addAll((Collection<?>) this.value);
			} else {
				if (this.value != null) {
					params.add(this.value);
					if (this.secondValue != null) {
						params.add(this.secondValue);
					}
				}
			}

			return this.sqlSign.getCondition(propertyAndColumnMap.get(this.propertyName), index, params.size() - index);
		}
	}
}