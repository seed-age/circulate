package com.sunnsoft.sloa.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 * 
 * @author chenjian
 *
 */
public class CloseableHttpClientUtils {
	private static final CloseableHttpClient httpclient;
	public static final String CHARSET = "UTF-8";

	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).build();
		httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}
	
	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url请求的url地址
	 *            ?之前的地址
	 * @param params请求的参数
	 * @param charset编码格式
	 * @return 页面内容
	 */
	public static String sendGet(String url, Map<String, Object> params)
			throws ParseException, UnsupportedEncodingException, IOException {

		if (params != null && !params.isEmpty()) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());

			for (String key : params.keySet()) {
				pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
		}
		System.out.println("URL: " + url);
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode != 200) {
			if(statusCode != 404) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			response.close();
			return result;
		} else {
			return null;
		}
	}
	
	
	public static String sendGetAndHeader(String url, String sessionId, String query, Map<String, Object> params)
			throws ParseException, UnsupportedEncodingException, IOException {
		
		if (params != null && !params.isEmpty()) {
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
			
			for (String key : params.keySet()) {
				pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
		}
		url += "&query=" + query;
		
		System.out.println("URL: " + url);
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Cookie", "X-LENOVO-SESS-ID="+sessionId+";");
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode != 200) {
			if(statusCode != 404) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			response.close();
			return result;
		} else {
			return null;
		}
	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url请求的url地址
	 *            ?之前的地址
	 * @param params请求的参数
	 * @param charset编码格式
	 * @return 页面内容
	 * @throws IOException
	 * @throws ClientProtocolException
	 */

	public static String sendPost(String url, Map<String, Object> params) throws ClientProtocolException, IOException {

		List<NameValuePair> pairs = null;
		if (params != null && !params.isEmpty()) {
			pairs = new ArrayList<NameValuePair>(params.size());
			for (String key : params.keySet()) {
				pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
		}
		HttpPost httpPost = new HttpPost(url);
		if (pairs != null && pairs.size() > 0) {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
		}
		CloseableHttpResponse response = httpclient.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			httpPost.abort();
			throw new RuntimeException("HttpClient,error status code :" + statusCode);
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			response.close();
			return result;
		} else {
			return null;
		}
	}

	/*@Test
	public void testGet() throws ParseException, UnsupportedEncodingException, IOException, NoSuchAlgorithmException {
		String sessionId = getSession();
		System.out.println(sessionId);
		
		String url = "http://box.seedland.cc/v2/sso/token/";
		Map<String, Object> params01 = new HashMap<>();
		String userSlug = "cs";
		Long timestamp = System.currentTimeMillis();
		String magic = "LENOVOCLOUD";
		params01.put("user_slug", userSlug);// 用户登录名
		params01.put("timestamp", timestamp); // 当前时间戳
		// 计算得出 token
		String token = userSlug + "_" + magic + "_" + timestamp;
		String lenovo_token = MD5(token);
		params01.put("lenovo_token", lenovo_token);
		String sendGet = sendGet(url, params01);
		System.out.println(sendGet(url, params01));

	}*/

	/**
	 * 请求Http post类型URL
	 * 
	 * @param url
	 *            请求Url
	 * @param map
	 *            请求参数
	 * @return
	 */
	/*
	 * public static String fireHttpPost(String url, Map<String, Object> map) {
	 * 
	 * String json = "";
	 * 
	 * HttpClient client = new HttpClient(); PostMethod postMethod = new
	 * PostMethod(url); postMethod.addRequestHeader("Content-Type",
	 * "application/json;charset=UTF-8"); Iterator iterator =
	 * map.entrySet().iterator(); while (iterator.hasNext()) { Entry<String, Object>
	 * elem = (Entry<String, Object>) iterator.next();
	 * postMethod.addParameter(elem.getKey(), elem.getValue().toString()); } try {
	 * int statusCode = client.executeMethod(postMethod); if (statusCode ==
	 * HttpStatus.SC_OK) { json = postMethod.getResponseBodyAsString(); } } catch
	 * (HttpException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return json;
	 * 
	 * }
	 * 
	 *//**
		 * 请求Http get类型URL
		 * 
		 * @param url
		 *            请求Url
		 * @return
		 *//*
			 * public static String fireHttpGet(String url) { String json = ""; HttpClient
			 * client = new HttpClient(); GetMethod getMethod = new GetMethod(url);
			 * getMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
			 * try { int statusCode = client.executeMethod(getMethod); if (statusCode ==
			 * HttpStatus.SC_OK) { json = getMethod.getResponseBodyAsString(); } } catch
			 * (HttpException e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 * catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } return json; }
			 */

}
