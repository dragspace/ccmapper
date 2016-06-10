package com.ccmapper.core.example;

import com.ccmapper.core.utils.CCStringUtils;

public class ExampleConstant {
	
	/**
	 * 参数的索引位置
	 */
	public static final String PREFIX_PARAM = "";
	
	public static final String AND = " AND ";
	public static final String OR = " OR ";
	
	public static final GenerateSql NOVALUE_GENERATESQL = new GenerateSql(){
		@Override
		public String generate(SqlSign sqlSign, String columnName, int index, int valuesSize) {
			return columnName + sqlSign;
		}
	};
	
	public static final GenerateSql COMMON_GENERATESQL = new GenerateSql(){
		@Override
		public String generate(SqlSign sqlSign, String columnName, int index, int valuesSize) {
			return columnName + sqlSign + "#{"+PREFIX_PARAM + index +"}";
		}
	};
	
	public static final GenerateSql BETWEEN_GENERATESQL = new GenerateSql(){
		@Override
		public String generate(SqlSign sqlSign, String columnName, int index, int valuesSize) {
			return columnName + sqlSign + "#{"+PREFIX_PARAM + index++ +"} AND #{"+ PREFIX_PARAM + index +"}" ;
		}
	};
	
	public static final GenerateSql IN_GENERATESQL = new GenerateSql(){
		@Override
		public String generate(SqlSign sqlSign, String columnName, int index, int valuesSize) {
			if(valuesSize == 0){
				return "";
			}
			
			StringBuilder inValues = new StringBuilder(columnName + sqlSign + " (");
			for(int i = 0; i < valuesSize; i++){
				inValues.append(" #{"+PREFIX_PARAM + (index++) +"} ,");
			}
			CCStringUtils.deleteEnd(inValues, ",");
			
			inValues.append(")");
			System.out.println(inValues);
			return inValues.toString() ;
		}
	};
	 
}
