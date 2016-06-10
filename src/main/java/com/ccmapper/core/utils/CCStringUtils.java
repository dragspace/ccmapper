package com.ccmapper.core.utils;

public class CCStringUtils {

	/**
	 * @Title: delete End 
	 * @Description: 删除sringbuilder 以end为结尾的字符串 
	 * @author xiaoruihu
	 * @param sb
	 * @param end
	 * @return
	 */
	public static StringBuilder deleteEnd(StringBuilder sb, String end){
		return sb.delete(sb.length() - end.length(), sb.length());
	}
}
