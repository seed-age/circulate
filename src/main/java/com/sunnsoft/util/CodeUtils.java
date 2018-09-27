package com.sunnsoft.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeUtils {
	/**
	 * 返回年月日时分秒毫秒格式的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String createCode(String str) {
		String code = "";
		if (str != null && str.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			code = str + sdf.format(new Date());
		}
		return code;
	}
}
