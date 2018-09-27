package com.sunnsoft.sloa.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.exception.BoxException;
import com.lenovo.css.lenovocloud.sdk.model.UserLoginModel;
import com.pactera.sso.client.utils.SsoUserUtil;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.db.vo.UserMssage;
import org.apache.http.ParseException;
import org.apache.struts2.ServletActionContext;
import org.gteam.util.FastJSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.pactera.sso.client.servlet.SsoUserUtil;

/**
 * 调用网盘的工具类
 * @author chenjian
 *
 */
public class LenovoCloudSDKUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(LenovoCloudSDKUtils.class);
	
	/**
	 * 获取当前SSO登录用户的登录名loginId
	 * @return
	 */
	public static String getLoginId() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String loginId = SsoUserUtil.getCurrentUser(request);
		LOGGER.warn("登录SSO的登录名： " + loginId);
		return loginId;
	}
	
	/**
	 * 返回网盘对象
	 * @return
	 * @throws Exception 
	 */
	public static LenovoCloudSDK getLenovoCloudSDK(Config config) {
		//创建网盘对象
		LenovoCloudSDK sdk = new LenovoCloudSDK();
		// 联系网盘(正式环境地址)
		String boxUrl  = config.getBoxUrl();
		LOGGER.warn("开始调用网盘接口::::::::  设置网盘URL地址: " + boxUrl);
		sdk.setBoxUrl(boxUrl);
		return sdk;
	};
	
	/**
	 * 返回system_OA用户登录网盘的session
	 * @return
	 */
	public static String getLenovoCloudSDKSession(LenovoCloudSDK sdk, Config config) {
		try {
			
			User user = Services.getUserService().createHelper().getAccountName().Eq("system").uniqueResult();
			//判断是否为null
			if(user == null) {
				User newUser = new User();
				return getBoxSession(sdk, config, newUser, 0);
			}else {
				
				//获取当前时间
				long now = System.currentTimeMillis();
				//存储网盘session的最后更新时间
				long sessionTime = user.getAccessTokenExpire().getTime();
				LOGGER.warn("获取当前时间: " + now + "  ::::::  获取网盘session最后的存储时间: " + sessionTime);
				//进行比较
				if((now - sessionTime) > 23*60*60*1000L) {
					LOGGER.warn("重新调用网盘的登录接口, 本地存储的session 已过期:::::::::::::::::::::::::::: ");
					return getBoxSession(sdk, config, user, 1);
				}else {
					LOGGER.warn("没有调用网盘的登录接口, 使用本地存储的session进行操作:::::::::::::::::::::::::::: ");
					return user.getAccessToken(); //返回的是网盘的session
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("调用网盘失败::::::::::::::::::::: 返回 NULL 值");
		}
		
		return null;
	}

	public static String getBoxSession(LenovoCloudSDK sdk, Config config, User user, int type)
			throws BoxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		// 设置正式账号和密码
		String userSlug = config.getUserSlug();
		String password = config.getPassword();
		LOGGER.warn("开始调用网盘登录接口::::::::  设置网盘登录的账号: " + userSlug + ",     设置网络登录的密码: " + password);
		// 调用登录方法, 设置账户和密码
		UserLoginModel login = sdk.login(userSlug, password);
		// 获取session
		String session = login.getXLENOVOSESSID();
		LOGGER.warn("调用网盘成功! :::::::::::::::::::::::::::::::::::::: SESSIONID: " + session);
		
		LOGGER.warn("调用网盘成功! ::::::::::::::::::::::::::::::::: 把session存储到数据库中: " + session);
		
		//获取当前时间并且减去一个小时
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		Date date = cal.getTime(); 
		if(type == 0) { //等于零 表示第一次设置网盘的session
			user.setAccessToken(session); //该字段存储网盘session
			user.setAccountName("system");
			user.setNickName("网盘system用户");
			user.setCreateTime(date);
			user.setUserPassword("12346");
			user.setAdmin(true);
			user.setEnabled(true);
			user.setPriority(0);
			user.setSoftDelete(true);
			user.setAccessTokenExpire(date);//该字段存储网盘的最后更新时间
			Services.getUserService().add(user);
		}else {
			user.setAccessToken(session); //该字段存储网盘session
			user.setAccessTokenExpire(date);//该字段存储网盘的最后更新时间
			Services.getUserService().update(user);
		}
		//mssage.setUpdateTime(date);
		//Services.getUserMssageService().update(mssage);
		LOGGER.warn("调用网盘成功! ::::::::::::::::::::::::::::::::: 存储成功");
		return session;
	}
	
	public final static String MD5(String pwd) throws NoSuchAlgorithmException {
		// 用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
		byte[] btInput = pwd.getBytes();

		// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
		MessageDigest mdInst = MessageDigest.getInstance("MD5");

		// MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
		mdInst.update(btInput);

		// 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
		byte[] md = mdInst.digest();

		// 把密文转换成十六进制的字符串形式
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) { // i = 0
			byte byte0 = md[i]; // 95
			str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
			str[k++] = md5String[byte0 & 0xf]; // F
		}

		// 返回经过加密后的字符串
		return new String(str);
	}
	
	/**
	 * 根据URL 获取当前SSO登录用户的session
	 * @param loginId
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, Object> getSessionAndS(String longId)
			throws ParseException, UnsupportedEncodingException, IOException, NoSuchAlgorithmException {
		//生产
		String url = "http://box.seedland.cc/v2/sso/token/";
		//测试
//		String url = "http://box-test.seedland.cc/v2/sso/token/";
		Map<String, Object> params = new HashMap<>();
		String userSlug = longId; // 当前用户的登录名
		//String userSlug = ""; // 当前用户的登录名
		//System.out.println("当前用户的登录名: " + userSlug);
		Long timestamp = System.currentTimeMillis(); // 当前时间戳
		String magic = "LENOVOCLOUD";
		// 计算得出 token
		String token = userSlug + "_" + magic + "_" + timestamp;
		String lenovo_token = MD5(token);
		params.put("user_slug", userSlug);
		params.put("timestamp", timestamp);
		params.put("lenovo_token", lenovo_token);
		
		String sendGet = CloseableHttpClientUtils.sendGet(url, params);
		Map<String, Object> map = new HashMap<String, Object>();
		
		//解析json格式
		JSONObject jsonObject = JSONObject.parseObject(sendGet);
		if(jsonObject.getString("code") != null) {
			if(jsonObject.getString("code").equals("user not exists")) {
				map.put("code", "401");
				return map;
			}
		}
		// 获取到key为xLenovoSessId的值
		String sessionId = jsonObject.getString("xLenovoSessId");
		String s = jsonObject.getString("s");
		
		map.put("session", sessionId);
		map.put("s", s);
		map.put("code", "200");
		
		return map;
	}
	/**
	 * 根据URL 获取当前SSO登录用户的session
	 * @param loginId
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSession()
			throws ParseException, UnsupportedEncodingException, IOException, NoSuchAlgorithmException {
		//生产
		//String url = "http://box.seedland.cc/v2/sso/token/";
		//测试
		String url = "http://box-test.seedland.cc/v2/sso/token/";
		Map<String, Object> params = new HashMap<>();
		String userSlug = getLoginId(); // 当前用户的登录名
//		String userSlug = "limin5"; // 测试时使用
		//System.out.println("当前用户的登录名: " + userSlug);
		Long timestamp = System.currentTimeMillis(); // 当前时间戳
		String magic = "LENOVOCLOUD";
		// 计算得出 token
		String token = userSlug + "_" + magic + "_" + timestamp;
		String lenovo_token = MD5(token);
		params.put("user_slug", userSlug);
		params.put("timestamp", timestamp);
		params.put("lenovo_token", lenovo_token);
		
		String sendGet = CloseableHttpClientUtils.sendGet(url, params);
		//解析json格式
		JSONObject jsonObject = JSONObject.parseObject(sendGet);
		if(jsonObject.getString("code") != null) {
			if(jsonObject.getString("code").equals("user not exists")) {
				return "401";
			}
		}
		// 获取到key为xLenovoSessId的值
		String sessionId = jsonObject.getString("xLenovoSessId");
		//System.out.println("当前用户的sessionId: " + sessionId);
		return sessionId;
	}
	
	/**
	 * 根据URL 获取当前SSO登录用户的session
	 * @param loginId
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSession(UserMssage mssage)
			throws ParseException, UnsupportedEncodingException, IOException, NoSuchAlgorithmException {
		
		if(mssage.getBoxSession() == null) {
			
			return getAPPBoxSession(mssage, 0);
		}else {
			
			//获取当前时间
			long now = System.currentTimeMillis();
			//存储网盘session的最后更新时间
			long sessionTime = mssage.getUpdateTime().getTime();
			LOGGER.warn("获取当前时间: " + now + "  ::::::  获取网盘session最后的存储时间: " + sessionTime);
			//进行比较
			if((now - sessionTime) > 23*60*60*1000L) {
				LOGGER.warn("重新调用网盘的登录接口, 本地存储的session 已过期:::::::::::::::::::::::::::: ");
				return getAPPBoxSession(mssage, 1);
			}else {
				LOGGER.warn("没有调用网盘的登录接口, 使用本地存储的session进行操作:::::::::::::::::::::::::::: ");
				return mssage.getBoxSession(); //返回的是网盘的session
			}
		}
	}

	public static String getAPPBoxSession(UserMssage mssage, int type)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
		//生产
		String url = "http://box.seedland.cc/v2/sso/token/";
		//测试
//		String url = "http://box-test.seedland.cc/v2/sso/token/";
		
		Map<String, Object> params = new HashMap<>();
		String userSlug = mssage.getLoginId(); // 当前用户的登录名
		LOGGER.warn(":::::::::::::::: APP端当前用户的登录名: " + userSlug);
//		String userSlug = "linmin5"; // 当前用户的登录名
		System.out.println(userSlug + ":::::::::::");
		Long timestamp = System.currentTimeMillis(); // 当前时间戳
		String magic = "LENOVOCLOUD"; 
		// 计算得出 token
		String token = userSlug + "_" + magic + "_" + timestamp;
		String lenovo_token = MD5(token);
		params.put("user_slug", userSlug);
		params.put("timestamp", timestamp);
		params.put("lenovo_token", lenovo_token);
		
		String sendGet = CloseableHttpClientUtils.sendGet(url, params);
		//解析json格式
		JSONObject jsonObject = JSONObject.parseObject(sendGet);
		if(jsonObject.getString("code") != null) {
			if(jsonObject.getString("code").equals("user not exists")) {
				return "401";
			}
		}
		// 获取到key为xLenovoSessId的值
		String sessionId = jsonObject.getString("xLenovoSessId");
		
		//获取当前时间并且减去一个小时
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		Date date = cal.getTime(); 
		//设置网盘session
		if(type == 0) {
			mssage.setCreateTime(date);
		}
		mssage.setBoxSession(sessionId);
		mssage.setUpdateTime(date);
		//更新
		Services.getUserMssageService().update(mssage);
		LOGGER.warn(":::::::::::::::: APP端  保存网盘session: " + sessionId + "  成功!!");
		
		return sessionId;
	}
	
	/**
	 * 调用网盘的查询接口，适用于移动端查询数据
	 * @param session
	 * @param s  
	 * @param path 文件夹目录
	 * @param pathType  路径类型
	 * @param limit 返回数目
	 * @param sort 排序方式
	 * @param query	搜索关键字
	 */
	public static String getAPPSearch(String session, String s, String path, String pathType, 
			int pageRows, int page, String sort, String query)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
		// 生成
		String url = "http://box.seedland.cc/v2/search/databox/" + path;
		//测试
//		String url = "http://box-test.seedland.cc/v2/search/databox/" + path;
		
		Map<String, Object> params = new HashMap<>();
		params.put("path_type", pathType);
		params.put("limit", pageRows);
		params.put("offset", page);
		params.put("sort", sort);
		//params.put("query", query);
		params.put("S", s);
		
		String sendGet = CloseableHttpClientUtils.sendGetAndHeader(url, session, query, params);
		
		//解析json格式的数据
		JSONObject jsonObject = JSONObject.parseObject(sendGet);
		//重构数据结构
		return getJsonData(jsonObject, page, pageRows);
	}

	/**
	 * 重构数据结构
	 * @param jsonObject
	 * @return
	 */
	public static String getJsonData(JSONObject jsonObject, int page, int pageRows) {
		Integer totalSize = jsonObject.getInteger("total_size");
		Integer contentSize = jsonObject.getInteger("content_size");
		JSONArray jsonArray = jsonObject.getJSONArray("content");
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			String desc = object.getString("desc");
			String neid = object.getString("neid");
			String rev = object.getString("rev");
			String mimeType = object.getString("mime_type");
			String path1 = object.getString("path");
			String pathType1 = object.getString("path_type");
			String size = object.getString("size");
			String isDir = object.getString("is_dir");
			String creatorUid = object.getString("creator_uid");
			
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("desc", desc);
			content.put("neid", neid);
			content.put("rev", rev);
			content.put("mimeType", mimeType);
			content.put("path", path1);
			content.put("pathType", pathType1);
			content.put("size", size);
			content.put("dir", isDir);
			content.put("creatorUid", creatorUid);
			contents.add(content);
			
		}
		
		int totalPage = 1;
		
		// 查询总数是否大于当前返回数量
		if(totalSize > contentSize) {
			// 计算出总页数
			totalPage = ((int) totalSize + pageRows - 1) / pageRows;
		}
		
		result.put("totalSize", totalSize); // 查询总数
		result.put("contentSize", contentSize); // 当前返回数据总数
		result.put("content", contents); //数据
		result.put("totalPage", totalPage); //总页数
		
		String json = FastJSONUtils.getJsonHelper().toJSONString(result);
		return json;
	}
}
