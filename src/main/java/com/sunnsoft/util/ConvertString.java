package com.sunnsoft.util;

/**
 * 
 * Description: <br/>
 * Copyright(C),2010-2013,James.zhou <br/>
 * This program is protected by copyright laws. <br/>
 * Program Name:json字符串数据替换和截取 <br/>
 * Date:2010-8-4
 * 
 * @author James.zhou james8172@foxmail.com
 * @version 1.0
 */
public class ConvertString {
	// public static void main(String[] args) {
	// String
	// auth="modId:1;funId:1,2,3;modId:1,2;funId:1,2,5,6;modId:1,2,5;funId:1,5,6;";
	// String auth2="modId:27;funId:2,3;";
	// ConvertString convertString=new ConvertString();
	// System.out.println(convertString.cutString("modId","funId",";",auth,
	// "1"));
	// System.out.println(convertString.invertString("modId","funId", ";", auth,
	// "1,2,5","1,2,3,4,5"));
	// System.out.println(convertString.hasString(auth,"modId:1wqe"));
	// System.out.println(convertString.invertString("modId","funId", ";",
	// auth2, "27","1,2,3,4,5"));
	// }
	/**
	 * 
	 * @param Str1
	 *            //原始字符串
	 * @param Str2
	 *            //包含的字符串
	 * @return
	 */
	public static boolean hasString(String Str1, String Str2) {
		int from = Str1.indexOf(Str2);
		if (from == -1) {
			return false;
		} else
			return true;

	}

	/**
	 * @param Str1
	 *            字符字段1
	 * @param Str2
	 *            字符字段2
	 * @param Str3
	 *            截取符标识
	 * @param Str4
	 *            原始的字符串
	 * @param Str5
	 *            需要截取的字符串值
	 * 
	 */
	public static String cutString(String Str1, String Str2, String Str3,
			String Str4, String Str5) {
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		String str_temp;
		String str_temp2;
		String str_temp3;
		String trueTre = Str1 + ":" + Str5;
		num = Str4.indexOf(trueTre);
		System.out.println(num);
		System.out.println(Str4);
		if (num == -1)
			return "";
		str_temp = Str4.substring(num, Str4.length());
		num2 = str_temp.indexOf(Str3) + 1;
		str_temp2 = str_temp.substring(num2, str_temp.length());
		System.out.println("2" + str_temp2);
		num3 = str_temp2.indexOf(Str3) + 1;
		str_temp3 = str_temp2.substring(0, num3);
		System.out.println(str_temp3);
		String str_temp4 = str_temp3.substring(Str2.length() + 1, str_temp3
				.length() - 1);
		return str_temp4;
	}

	/**
	 * 
	 * @param Str1
	 *            字符字段1
	 * @param Str2
	 *            字符字段2
	 * @param Str3
	 *            截取符标识
	 * @param Str4
	 *            原始的字符串
	 * @param Str4
	 *            需要截取的字符串值
	 * @param Str4
	 *            需要替换的字符串值
	 * @return
	 */
	// System.out.println(authTest.conventStringV("modId", ";", auth,
	// "1","1,2,3,4,5"));
	public static String invertString(String Str1, String Str2, String Str3,
			String Str4, String Str5, String Str6) {
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		String str_temp;
		String str_temp2;
		String str_temp3;
		String trueTre = Str1 + ":" + Str5;
		num = Str4.indexOf(trueTre);
		System.out.println(trueTre);
		String str_temp0 = Str4.substring(0, num);
		str_temp = Str4.substring(num, Str4.length());
		num2 = str_temp.indexOf(Str3) + 1;
		str_temp2 = str_temp.substring(num2, str_temp.length());
		num3 = str_temp2.indexOf(Str3) + 1;
		System.out.println(str_temp2);
		num3 = str_temp2.indexOf(Str3) + 1;
		str_temp3 = str_temp2.substring(num3, str_temp2.length());
		System.out.println(str_temp3);
		StringBuffer buf = new StringBuffer();
		buf.append(str_temp0);
		buf.append(trueTre);
		buf.append(Str3);
		buf.append(Str2);
		buf.append(":");
		buf.append(Str6);
		buf.append(Str3);
		buf.append(str_temp3);
		return buf.toString();
	}

	/**
	 * 将字符串转换为数字型数组
	 * 
	 * @param s
	 *            原始字符串
	 * @param tag
	 *            标识符
	 * @return Integer数组
	 */
	public static Integer[] stringToIntegers(String s, String tag) {
		String[] ss = s.split(tag);
		Integer[] ints = new Integer[ss.length];
		for (int i = 0; i < ss.length; i++) {
			ints[i] = Integer.parseInt(ss[i]);
		}
		return ints;
	}
}
