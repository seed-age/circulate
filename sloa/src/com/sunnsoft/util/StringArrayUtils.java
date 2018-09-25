package com.sunnsoft.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 林宇民 Andy (llade)
 *
 */
public class StringArrayUtils {

	public static List<String> string2List(String arrayString) {
		List<String> result = new ArrayList<String>();
		if(StringUtils.isEmpty(arrayString)) {
			return result;
		}
		if(! arrayString .startsWith("[") || !arrayString.endsWith("]")) {
			return result;
		}
		
		String tmp = arrayString.substring(1, arrayString.length()-1);
		if(StringUtils.isBlank(tmp)) {
			return result;
		}
		String[] array = tmp.split(",");
		for (int i = 0; i < array.length; i++) {
			if(StringUtils.isBlank(array[i])) {
				continue;
			}
			if(	!(array[i] .startsWith("'") &&  array[i] .endsWith("'") )
					&&
				!(array[i] .startsWith("\"") &&  array[i] .endsWith("\"") )	
			) {
				throw new IllegalArgumentException("数组格式有误");
			}
			result.add(array[i].substring(1, array[i].length()-1));
		}
		
		return result;
		
	}
}
