package com.sunnsoft.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JsonLibTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] intArray = { 1, 2, 3 };
		Map map = new HashMap();
		map.put("name", "json");
		map.put("bool", Boolean.TRUE);
		map.put("int", new Integer(1));
		map.put("arr", new String[] { "a", "b" });
		map.put("func", "function(i){ return this.arr[i]; }");
		JSONObject jsonObject = JSONObject.fromObject(map);
		JSONArray jsonArray = JSONArray.fromObject(intArray);
		System.out.println(jsonObject);
		System.out.println(jsonArray);
		//测试json-lib处理速度
//		UserInfo userInfo = new UserInfo();
//		userInfo.setEmail("a@a");
//		
//		User user = new User();
//		user.setAccountName("aa");
//		user.setCreateTime(new Date());
//		user.setStatus(true);
//		user.setUserInfo(userInfo);
//		List list = new ArrayList(10000);
//		long start = System.currentTimeMillis();
//		for(int i = 0; i <= 10000; i++){
//			user.setAccountName(user.getAccountName()+i);
//			JSONObject j = JSONUtils.getJsonHelper().setDateFormat("yyyy-MM-dd HH:mm:ss").toJSONObject(user);
//			list.add(j);
//		}
//		long end = System.currentTimeMillis();
//		//System.out.println(list);
//		int i = 0 ;
//		System.out.println(end - start);
//		for (Iterator iterator = list.iterator(); iterator.hasNext(); i++) {
//			Object object = (Object) iterator.next();
//			if(i % 1000 == 0){
//				//System.out.println(object);
//			}
//		}
		//测试结果：用json-lib处理一个对象大概0.6毫秒作用，的确太慢。
		
		//fastjson解析数据
		com.alibaba.fastjson.JSONArray array = (com.alibaba.fastjson.JSONArray) JSON.parse("[{'test':'aaa','test2':false},{'test':'bbb','test2':true}]");
		List<Map<String, Object>> l = JSON.parseObject("[{'test':'aaa','test2':false},{'test':'bbb','test2':true}]", new TypeReference<List<Map<String, Object>>>(){});
		System.out.println(l);
		System.out.println(l.get(0).get("test2"));
	}

}
