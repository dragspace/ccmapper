package com.ccmapper.core.example;

public class ExampleConstant {
	
	/**
	 * 参数的索引位置
	 */
	public static final String PREFIX_PARAM = "";
	
	public static final String AND = " AND ";
	public static final String OR = " OR ";
	
	public static final GenerateSql DEFAULT_GENERATESQL = new GenerateSql(){
		@Override
		public String generate(SqlSign sqlSign, String columnName, int index) {
			return columnName + sqlSign + "#{"+PREFIX_PARAM + index+"}";
		}
	};
	
	public static final GenerateSql DEFAULT_BETWEEN_GENERATESQL = new GenerateSql(){
		@Override
		public String generate(SqlSign sqlSign, String columnName, int index) {
			return columnName + sqlSign + "#{"+PREFIX_PARAM + (++index) +"} AND #{"+ PREFIX_PARAM + (++index) +"}" ;
		}
	};
}
