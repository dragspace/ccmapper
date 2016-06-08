package com.ccmapper.core.example;

/**
 * @Description: GenerateSql  处理特定sql函数
 * @author xiaoruihu 2016年6月6日 下午5:45:15
 */
public  interface GenerateSql{
	public String generate(SqlSign sqlSign, String columnName, int index);
}
