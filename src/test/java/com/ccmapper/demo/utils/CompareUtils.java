package com.ccmapper.demo.utils;

import java.util.List;

public class CompareUtils {

	/**
	 * @Title: isEqual 
	 * @Description: access T's equels copare list
	 * @author xiaoruihu
	 * @param src
	 * @param dest
	 * @return
	 */
	public static <T> boolean isListEqual(List<T> src, List<T> dest){

		int length = src.size();
		
		if(length != dest.size()){
			return false;
		}
		for(int i = 0; i < length; i++){
			if(!src.get(i).equals(dest.get(i))){
				return false;
			}
		}
		return true;
	}
}
