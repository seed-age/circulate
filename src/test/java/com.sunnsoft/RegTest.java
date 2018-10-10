package com.sunnsoft;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String reg = "[_]\\d\\.shtml$";
		String value = "http://localhost/shop/front/news/page-more_2.shtml";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(value);
		System.out.println(matcher.matches() + "," + matcher.regionStart());

		int index = value.indexOf("_");
		System.out.println(index);

		String start = value.substring(0, index);
		System.out.println(start);

		String end = value.substring(index);
		System.out.println(end);

		end = end.replace("_", "");
		end = end.replace(".shtml", "");

		System.out.println(end);

		System.out.println(start + ".action?toPage=" + end);

		// ^(.+)[_](\\d)\\.shtml$ $1.action?toPage=$2 [R]
		
		String listString = "User.dictionariesForCreater[key_category desc],User.dictionariesForUpdater[key_category desc]";
		Map<String,String> result = new HashMap<String,String>();
		Pattern p = Pattern.compile("([^\\[]+)\\[([^\\]]+)\\]"); 
		if(StringUtils.isNotEmpty(listString)){
			String[] list = listString.split(",");
			for (int i = 0; i < list.length; i++) {
				Matcher m = p.matcher(list[i]);
				while(m.find()){
					result.put(m.group(1), m.group(2));
				}
			}
		}
		System.out.println("project.one.to.many.order.by:"+result);

	}

}
